package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.api.StorageApi;
import com.de.nkoepf.backend.api.model.StorageDto;
import com.de.nkoepf.backend.api.model.StorageOverviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.ok(storageService.getStorage(storageId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<String> addStorageForUser(String storageName) {
        return StorageApi.super.addStorageForUser(storageName);
    }
}
