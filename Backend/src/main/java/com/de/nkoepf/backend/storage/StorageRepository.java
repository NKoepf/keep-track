package com.de.nkoepf.backend.storage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StorageRepository extends CrudRepository<Storage, Long> {

    List<Storage> findAllByOwner_Id(Long userId);

    Storage findStorageById(Long storageId);

    @Query(nativeQuery = true, value = "SELECT id FROM keep_track.public.storage WHERE owner_id = :userId")
    List<Long> getStorageIdsOfUser(Long userId);
}
