package com.de.nkoepf.backend.auth;

import com.de.nkoepf.backend.api.model.LoginRequestDto;
import com.de.nkoepf.backend.api.model.LoginResponseDto;
import com.de.nkoepf.backend.user.StorageUser;
import com.de.nkoepf.backend.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;


    public LoginFilter(
            String url,
            JwtUtil jwtUtil,
            AuthenticationManager authManager,
            UserService userService) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        LoginRequestDto loginRequestModel = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        log.info("login attempt for user {}", loginRequestModel.getEmail());
        return userService.tryGetUserPassAuthToken(loginRequestModel.getEmail(), loginRequestModel.getPassword());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException {
        if (!auth.isAuthenticated()) {
            log.info("failed login attempt for user {}", auth.getPrincipal());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().flush();
        } else {
            UserDetails userDetails =
                    userService.loadUserByUsername(auth.getPrincipal().toString());

            UserTokenData userTokenData = UserTokenData.builder()
                    .email(userDetails.getUsername())
                    .surName(((StorageUser) userDetails).getSurname())
                    .name(((StorageUser) userDetails).getName())
                    .authorities(userDetails.getAuthorities())
                    .build();

            final String jwt = jwtUtil.generateToken(userTokenData);

            final LoginResponseDto loginResponseModel = LoginResponseDto.builder()
                    .jwt(jwt)
                    .build();

            String body = new ObjectMapper().writeValueAsString(loginResponseModel);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(body);
            response.getWriter().flush();
        }
    }

}
