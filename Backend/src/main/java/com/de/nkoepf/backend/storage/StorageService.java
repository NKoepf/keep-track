package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.api.model.ProductDto;
import com.de.nkoepf.backend.api.model.StorageDto;
import com.de.nkoepf.backend.api.model.StorageEntryDto;
import com.de.nkoepf.backend.api.model.StorageOverviewDto;
import com.de.nkoepf.backend.storage.entry.StorageEntry;
import com.de.nkoepf.backend.storage.entry.StorageEntryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final ObjectMapper mapper;

    private final StorageEntryRepository entryRepository;
    private final StorageRepository storageRepository;


    /***
     * Get overview objects for all storages of the specific user
     * @param userId ID of the user, for which all storage overviews will be retrieved
     * @return all storage overviews for given user id
     */
    public List<StorageOverviewDto> getStorageOverviewsForUser(Long userId) {
        List<Storage> storagesForUser = storageRepository.findAllByOwner_Id(userId);
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
    public StorageDto getStorage(Long storageId) {
        Storage storage = storageRepository.findStorageById(storageId);
        return storageToStorageDto(storage);
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
}
