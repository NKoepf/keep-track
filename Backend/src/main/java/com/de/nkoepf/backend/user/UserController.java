package com.de.nkoepf.backend.user;

import com.de.nkoepf.backend.api.UserApi;
import com.de.nkoepf.backend.api.model.StorageDto;
import com.de.nkoepf.backend.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final StorageService storageService;
    private final UserService userService;

    @Override
    public ResponseEntity<StorageDto> getStorageOfUserByStorageId(String userId, String storageId) {
        return UserApi.super.getStorageOfUserByStorageId(userId, storageId);
    }

    @Override
    public ResponseEntity<List<StorageDto>> getStoragesOfUser(String userId) {


        return UserApi.super.getStoragesOfUser(userId);
    }
}
