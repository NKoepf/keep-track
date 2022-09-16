package com.de.nkoepf.backend.storage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends CrudRepository<Storage, Long> {

    List<Storage> findAllByOwner_Id(Long userId);

    List<Storage> findAllByOwner_Email(String email);

    Optional<Storage> findStorageById(Long storageId);

    @Query(nativeQuery = true, value = "SELECT id FROM keep_track.public.storage WHERE owner_id = :userId")
    List<Long> getStorageIdsOfUser(Long userId);

    boolean existsStorageByNameAndOwnerId(String storageName, Long ownerId);
}
