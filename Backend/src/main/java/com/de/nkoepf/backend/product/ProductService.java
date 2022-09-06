package com.de.nkoepf.backend.product;

import com.de.nkoepf.backend.api.model.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper mapper;

    ProductDto getProductDto(String barCode) {
        var product = productRepository.findProductByBarCode(barCode);
        return mapper.convertValue(product, ProductDto.class);
    }

    ResponseEntity<String> addProduct(ProductDto productDto) {
        if (productRepository.findProductByBarCode(productDto.getBarCode()) == null) {
            Product savedProduct = productRepository.save(mapper.convertValue(productDto, Product.class));
            log.info("saved {}", savedProduct);
            return ResponseEntity.ok("New Product saved");
        } else {
            return ResponseEntity.status(409).body("The given product already exists");
        }
    }


}
