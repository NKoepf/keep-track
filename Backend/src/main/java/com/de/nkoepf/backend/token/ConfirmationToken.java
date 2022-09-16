package com.de.nkoepf.backend.token;

import com.de.nkoepf.backend.user.StorageUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String confirmationToken;

    private LocalDate createdDate;

    @OneToOne(targetEntity = StorageUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private StorageUser user;

    public ConfirmationToken(StorageUser user) {
        this.user = user;
        this.createdDate = LocalDate.now();
        this.confirmationToken = UUID.randomUUID().toString();
    }
}
