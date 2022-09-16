package com.de.nkoepf.backend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTokenData {
    private String name;
    private String surName;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
}
