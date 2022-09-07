package com.de.nkoepf.backend.user;

import com.de.nkoepf.backend.api.UserApi;
import com.de.nkoepf.backend.api.model.StorageOverviewDto;
import com.de.nkoepf.backend.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final StorageService storageService;

    @Override
    public ResponseEntity<List<StorageOverviewDto>> getStorageOverviewsOfUser(Long userId) {
        return ResponseEntity.ok(storageService.getStorageOverviewsForUser(userId));
    }
}
