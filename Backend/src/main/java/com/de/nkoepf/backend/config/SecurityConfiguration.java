package com.de.nkoepf.backend.config;

import com.de.nkoepf.backend.auth.JwtRequestFilter;
import com.de.nkoepf.backend.auth.JwtUtil;
import com.de.nkoepf.backend.auth.LoginFilter;
import com.de.nkoepf.backend.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .antMatchers("/user/register/**")
                .permitAll()
            .and()
                .authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
//                .csrf().requireCsrfProtectionMatcher(request->
//                    !request.getServletPath().contains("/login"))
//            .and()
                .csrf().disable()
                .addFilterBefore(new LoginFilter("/login", jwtUtil, authenticationManager, userService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        return http.build();
        // @formatter:on
    }
}
