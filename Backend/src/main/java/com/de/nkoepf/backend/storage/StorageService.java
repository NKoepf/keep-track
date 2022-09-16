package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.api.model.ProductDto;
import com.de.nkoepf.backend.api.model.StorageDto;
import com.de.nkoepf.backend.api.model.StorageEntryDto;
import com.de.nkoepf.backend.api.model.StorageOverviewDto;
import com.de.nkoepf.backend.config.WebSecurityConfig;
import com.de.nkoepf.backend.product.Product;
import com.de.nkoepf.backend.product.ProductRepository;
import com.de.nkoepf.backend.storage.entry.StorageEntry;
import com.de.nkoepf.backend.storage.entry.StorageEntryRepository;
import com.de.nkoepf.backend.user.StorageUser;
import com.de.nkoepf.backend.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final ObjectMapper mapper;

    private final StorageEntryRepository entryRepository;
    private final StorageRepository storageRepository;
    public final static String NO_PERMISSION_STORAGE = "Current user has no permission to access storage ";
    private final ProductRepository productRepository;
    private final UserService userService;

    /***
     * Get overview objects for all storages of the specific user
     * @param email email of the user, for which all storage overviews will be retrieved
     * @return all storage overviews for given user id
     */
    public List<StorageOverviewDto> getStorageOverviewsForUser(String email) {
        List<Storage> storagesForUser = storageRepository.findAllByOwner_Email(email);
        List<StorageOverviewDto> overviews = new ArrayList<>();

        storagesForUser.forEach(storage -> {
            StorageOverviewDto overview = new StorageOverviewDto();
            overview.id(storage.getId());
            overview.name(storage.getName());
            overview.owner(storage.getOwner().getId());
            overviews.add(overview);
        });
        return overviews;
    }

    /***
     * Retrieval of specific StorageDto for given storage id
     * @param storageId ID of the requested storage
     * @return requested storage as StorageDto
     */
    public StorageDto getStorageDto(Long storageId) {
        Optional<Storage> storage = storageRepository.findStorageById(storageId);
        if (storage.isEmpty()) {
            throw new NoSuchElementException();
        }
        return storageToStorageDto(storage.get());
    }

    /***
     * Mapping of the internal Store object to StoreDto
     * @param store Store object to be mapped to StoreDto
     * @return mapped StoreDto
     */
    public StorageDto storageToStorageDto(Storage store) {
        StorageDto dto = new StorageDto();
        dto.id(store.getId());
        dto.name(store.getName());
        dto.owner(store.getOwner().getId());
        getStorageContent(dto);

        return dto;
    }

    /***
     * Mapping of the internal StoreEntry object to StoreEntryDto
     * @param entry Entry to be mapped to StorageEntryDto
     * @return mapped StorageEntryDto
     */
    private StorageEntryDto storageEntryToStorageEntryDto(StorageEntry entry) {
        return StorageEntryDto.builder()
                .storageId(entry.getId())
                .product(mapper.convertValue(entry.getProduct(), ProductDto.class))
                .amount(entry.getAmount())
                .id(entry.getId())
                .build();
    }

    /***
     * Fill the storageDto object with all products of the storage
     * @param storageDto the storage to be filled
     */
    private void getStorageContent(StorageDto storageDto) {
        List<StorageEntry> entries = entryRepository.findAllStorageEntryByStorageId(storageDto.getId());
        storageDto.setProducts(new ArrayList<>());
        entries.forEach(entry -> {
            storageDto.getProducts().add(StorageEntryDto.builder()
                    .product(mapper.convertValue(entry.getProduct(), ProductDto.class))
                    .amount(entry.getAmount())
                    .storageId(storageDto.getId())
                    .id(entry.getId())
                    .build());
        });
    }

    /***
     * Create new storage with the given name for the currently logged-in user
     * @param storageName name which the new storage will have
     * @throws IllegalArgumentException throws IllegalArgumentException if storage already exists
     */
    void createStorage(String storageName) throws IllegalArgumentException {
        String email = WebSecurityConfig.getCurrentUserMail();
        StorageUser user = (StorageUser) userService.loadUserByUsername(email);
        if (storageRepository.existsStorageByNameAndOwnerId(storageName, user.getId())) {
            throw new IllegalArgumentException("storage with name "
                    + storageName + " already exists for user " + user);
        }
        Storage storage = new Storage();
        storage.setName(storageName);
        storage.setOwner(user);
        storageRepository.save(storage);
    }


    public StorageEntryDto changeAmount(Long storageId, String barcode, Integer amount, boolean absolute) throws IllegalAccessException {
        if (!hasAccess(storageId))
            throw new IllegalAccessException(NO_PERMISSION_STORAGE + storageId);

        StorageEntry entry = entryRepository.findStorageEntryByStorageAndProduct_BarCode(storageId, barcode);
        if (absolute) {
            entry.setAmount(amount);
        } else {
            entry.setAmount(Math.max(0, entry.getAmount() + amount));
        }

        return storageEntryToStorageEntryDto(entryRepository.save(entry));
    }

    /***
     * Checks if current user is allowed to access given storage
     * @param storageId of the storage to edit
     * @return true if user is authorized, false if user is unauthorized to access store
     */
    private boolean hasAccess(Long storageId) {
        String email = WebSecurityConfig.getCurrentUserMail();
        List<Long> storagesOfCurrentUser = new ArrayList<>();
        getStorageOverviewsForUser(email).forEach(storage -> storagesOfCurrentUser.add(storage.getId()));

        // If current user is owner of teh requested storage, it is allowed to access and edit
        if (storagesOfCurrentUser.contains(storageId)) return true;
        else return false;
    }

    public boolean addProductToStorage(Long storageId, String barcode) throws NoSuchElementException, IllegalAccessException, IllegalArgumentException {
        if (!hasAccess(storageId)) {
            throw new IllegalAccessException(NO_PERMISSION_STORAGE + storageId);
        }

        Storage storage = getStorage(storageId);

        if (entryAlreadyExists(storageId, barcode)) return false;

        Product product = productRepository.findProductByBarCode(barcode);
        if (product == null) throw new NoSuchElementException();

        StorageEntry entry = new StorageEntry();
        entry.setProduct(product);
        entry.setStorage(storage);
        entry.setAmount(1);
        entryRepository.save(entry);
        return true;
    }

    public void deleteStorage(Long storageId) throws IllegalAccessException {
        if (hasAccess(storageId)) {
            storageRepository.deleteById(storageId);
        } else {
            throw new IllegalAccessException();
        }
    }

    public void removeProductFromStore(Long storageId, String barcode) throws IllegalAccessException {
        if (!hasAccess(storageId)) {
            throw new IllegalAccessException(NO_PERMISSION_STORAGE + storageId);
        }
        entryRepository.deleteStorageEntryByStorage_IdAndProduct_BarCode(storageId, barcode);
    }

    private Storage getStorage(Long storageId) {
        Optional<Storage> storage = storageRepository.findStorageById(storageId);
        if (storage.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return storage.get();
        }
    }

    private boolean entryAlreadyExists(Long storageId, String barcode) {
        return entryRepository.existsStorageEntriesByStorage_IdAndAndProduct_BarCode(storageId, barcode);
    }

}
