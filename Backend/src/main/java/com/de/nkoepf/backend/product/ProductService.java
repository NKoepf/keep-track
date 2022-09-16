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

    /***
     * Get the ProductDto of the given barcode
     * @param barCode Barcode of the requested product
     * @return ProductDto corresponding to the given barcode
     */
    ProductDto getProductDto(String barCode) {
        var product = productRepository.findProductByBarCode(barCode);
        return mapper.convertValue(product, ProductDto.class);
    }

    /***
     * Add new product to the system. If it is already in the database, process will be skipped.
     * @param productDto Product to be added to the database
     * @return HTTP 200 if successfully added to database.<br>
     * HTTP 409 if the product is already in the database.
     */
    ResponseEntity<String> addProduct(ProductDto productDto) {
        if (productRepository.findProductByBarCode(productDto.getBarCode()) == null) {
            Product savedProduct = productRepository.save(mapper.convertValue(productDto, Product.class));
            log.info("saved {}", savedProduct);
            return ResponseEntity.ok("New Product saved");
        } else {
            return ResponseEntity.status(409).body("The given product already exists");
        }
    }


    public void changeProduct(String barCode, ProductDto productDto) {
        Product product = productRepository.findProductByBarCode(productDto.getBarCode());
        if (product != null) {
            product.setBarCode(barCode);
            product.setProductName(productDto.getProductName());
            product.setManufacturer(productDto.getManufacturer());

            productRepository.save(product);
        } else {
            throw new IllegalArgumentException();
        }

    }
}
