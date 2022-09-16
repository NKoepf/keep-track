package com.de.nkoepf.backend.config;

import com.de.nkoepf.backend.api.model.UserRoleDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, UserRoleDto>() {
            @Override
            public UserRoleDto convert(String source) {
                return UserRoleDto.valueOf(source.toUpperCase());
            }
        });
    }
}
