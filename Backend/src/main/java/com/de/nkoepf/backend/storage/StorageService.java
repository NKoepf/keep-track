package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.storage.entry.StorageEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageEntryRepository storageRepository;


}
