package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StorageController {

    private final ProductRepository productRepository;


}
