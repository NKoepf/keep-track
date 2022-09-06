package com.de.nkoepf.backend.storage;

import com.de.nkoepf.backend.user.StorageUser;

import javax.persistence.*;

@Entity
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private StorageUser owner;

}
