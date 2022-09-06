package com.de.nkoepf.backend.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findProductByBarCode(String barcode);
}
