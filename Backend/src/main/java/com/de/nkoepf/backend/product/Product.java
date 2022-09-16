package com.de.nkoepf.backend.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "product")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String barCode;

    @JsonProperty("productName")
    private String productName;

    @Column
    private String manufacturer;

}
