package com.de.nkoepf.backend.product;

import com.de.nkoepf.backend.api.ProductApi;
import com.de.nkoepf.backend.api.model.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> getProduct(String barCode) {
        return ResponseEntity.ok(productService.getProductDto(barCode));
    }

    @Override
    public ResponseEntity<String> postProduct(ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    /**
     * PATCH /product
     * Change product
     *
     * @param barCode    (required)
     * @param productDto (required)
     * @return Successfully change product (status code 200)
     * or Server error (status code 500)
     */
    @Override
    public ResponseEntity<String> changeProduct(String barCode, ProductDto productDto) {
        productService.changeProduct(barCode, productDto);
        return ResponseEntity.ok("updated product " + productDto.getProductName());
    }
}
