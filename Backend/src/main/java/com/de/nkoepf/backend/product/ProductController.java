package com.de.nkoepf.backend.product;

import com.de.nkoepf.backend.api.ProductApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    ProductRepository productRepository;

    @Override
    public ResponseEntity<String> getProduct() {
        return ResponseEntity.ok(productRepository.findAll().get(0).toString());
    }
}
