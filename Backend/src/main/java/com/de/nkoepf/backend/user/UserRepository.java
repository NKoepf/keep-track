package com.de.nkoepf.backend.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<StorageUser, Long> {

    Optional<StorageUser> findByEmail(String email);

    Boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
