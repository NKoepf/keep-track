package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.user.StorageUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private StorageUser owner;

    @Column(name = "name")
    private String name;

}
