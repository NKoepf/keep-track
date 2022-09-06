package com.de.nkoepf.backend.storage.entry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageEntryRepository extends CrudRepository<StorageEntry, Long> {

    List<StorageEntry> findAllStorageEntryById(Long id);

}
