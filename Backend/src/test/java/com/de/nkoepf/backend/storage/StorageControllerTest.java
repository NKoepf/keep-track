package com.de.nkoepf.backend.storage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
class StorageControllerTest {

    @Test
    void getStorageOverviewsOfUser() {
    }

    @Test
    void getStorageByStorageId() {
    }

    @Test
    void addStorageForUser() {
    }

    @Test
    void updateProductAmount() {
    }

    @Test
    void addProductToStorage() {
    }

    @Test
    void decreaseProductAmount() {
    }

    @Test
    void deleteStorage() {
    }

    @Test
    void increaseProductAmount() {
    }

    @Test
    void removeProductFromStorage() {
    }
}