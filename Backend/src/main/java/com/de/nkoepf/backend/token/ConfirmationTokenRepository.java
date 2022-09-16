package com.de.nkoepf.backend.token;

import com.de.nkoepf.backend.user.StorageUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);

    Optional<ConfirmationToken> findConfirmationTokenByUser(StorageUser user);
}