package com.de.nkoepf.backend.product;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "bar_code")
    private String barCode;

    @Column(name = "product_name")
    private String productName;

    @Column
    private String manufacturer;


}
