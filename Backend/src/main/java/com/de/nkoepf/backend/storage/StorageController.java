package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.api.StorageApi;
import com.de.nkoepf.backend.api.model.StorageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StorageController implements StorageApi {

    private final StorageService storageService;

    @Override
    public ResponseEntity<StorageDto> getStorageByStorageId(Long storageId) {
        return ResponseEntity.ok(storageService.getStorage(storageId));

    }
}
