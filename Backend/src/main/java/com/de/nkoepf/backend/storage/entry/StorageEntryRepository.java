package com.de.nkoepf.backend.storage.entry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageEntryRepository extends CrudRepository<StorageEntry, Long> {

    List<StorageEntry> findAllStorageEntryByStorageId(Long storageId);

    StorageEntry findStorageEntryByStorageAndProduct_BarCode(Long Storage, String barcode);

    Boolean existsStorageEntriesByStorage_IdAndAndProduct_BarCode(Long storageId, String barcode);

    void deleteStorageEntryByStorage_IdAndProduct_BarCode(Long storageId, String barcode);
}
