package com.de.nkoepf.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keep-track")
@Getter
@Setter
public class KeepTrackProperties {
    private String jwtSecret;
    private Long jwtExpirationMs;
}
