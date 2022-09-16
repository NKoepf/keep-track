package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.api.StorageApi;
import com.de.nkoepf.backend.api.model.StorageDto;
import com.de.nkoepf.backend.api.model.StorageEntryDto;
import com.de.nkoepf.backend.api.model.StorageOverviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class StorageController implements StorageApi {

    private final StorageService storageService;

    @Override
    public ResponseEntity<List<StorageOverviewDto>> getStorageOverviewsOfUser() {
        return ResponseEntity.ok(storageService.getStorageOverviewsForUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()));
    }

    @Override
    public ResponseEntity<StorageDto> getStorageByStorageId(Long storageId) {
        try {
            return ResponseEntity.ok(storageService.getStorageDto(storageId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<String> addStorageForUser(String storageName) {
        try {
            storageService.createStorage(storageName);
            return ResponseEntity.ok("New storage " + storageName + " created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "storage with name "
                            + storageName + " already exists for user "
                            + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
    }

    @Override
    public ResponseEntity<StorageEntryDto> updateProductAmount(Long storageId, String barcode, Integer amount) {
        try {
            return ResponseEntity.ok(storageService.changeAmount(storageId, barcode, amount, true));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * POST /storage/{storageId}/product/{barcode}
     * Add product to storage. Product identified by barcode.
     *
     * @param storageId (required)
     * @param barcode   (required)
     * @return Successfully added already created product to store (status code 200)
     * or the given barcode is not yet registered (status code 204)
     * or Server error (status code 500)
     */
    @Override
    public ResponseEntity<String> addProductToStorage(Long storageId, String barcode) {
        try {
            if (storageService.addProductToStorage(storageId, barcode)) {
                return ResponseEntity.ok("Product added to storage");
            } else {
                return ResponseEntity.ok("Product already in storage");
            }
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No permission to access storage " + storageId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No product for barcode found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No storage found for storageId " + storageId);
        }
    }

    /**
     * PATCH /storage/{storageId}/product/{barcode}/decrease
     * decreases or decreases amount of given product in given storage
     *
     * @param storageId (required)
     * @param barcode   (required)
     * @return Successfully decreased product amount in storage (status code 200)
     * or Server error (status code 500)
     */
    @Override
    public ResponseEntity<StorageEntryDto> decreaseProductAmount(Long storageId, String barcode) {
        try {
            return ResponseEntity.ok(storageService.changeAmount(storageId, barcode, -1, false));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * DELETE /storage/{storageId}
     * Deletes specific storage of user
     *
     * @param storageId (required)
     * @return Successfully deleted storage (status code 200)
     * or Server error (status code 500)
     */
    @Override
    @Transactional
    public ResponseEntity<String> deleteStorage(Long storageId) {
        try {
            storageService.deleteStorage(storageId);
            return ResponseEntity.ok("successfully deleted storage " + storageId);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no permission to delete storage " + storageId);
        }
    }

    /**
     * PATCH /storage/{storageId}/product/{barcode}/increase
     * increases or decreases amount of given product in given storage
     *
     * @param storageId (required)
     * @param barcode   (required)
     * @return Successfully increased product amount in storage (status code 200)
     * or Server error (status code 500)
     */
    @Override
    public ResponseEntity<StorageEntryDto> increaseProductAmount(Long storageId, String barcode) {
        try {
            return ResponseEntity.ok(storageService.changeAmount(storageId, barcode, 1, false));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * DELETE /storage/{storageId}/product/{barcode}
     * Remove product from storage
     *
     * @param storageId (required)
     * @param barcode   (required)
     * @return Successfully removed product from store (status code 200)
     * or Server error (status code 500)
     */
    @Override
    public ResponseEntity<String> removeProductFromStorage(Long storageId, String barcode) {
        try {
            storageService.removeProductFromStore(storageId, barcode);
            return ResponseEntity.ok("Product removed from " + storageId);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
