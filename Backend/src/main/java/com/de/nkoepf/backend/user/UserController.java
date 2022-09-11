package com.de.nkoepf.backend.user;

import com.de.nkoepf.backend.api.UserApi;
import com.de.nkoepf.backend.api.model.RegisterRequestDto;
import com.de.nkoepf.backend.token.ConfirmationToken;
import com.de.nkoepf.backend.token.ConfirmationTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;
    private final ObjectMapper mapper;

    @Override
    public ResponseEntity<String> addUser(RegisterRequestDto registerRequestDto) {
        try {
            userService.signUpUser(mapper.convertValue(registerRequestDto, StorageUser.class));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The given user is already registered");
        }
        return ResponseEntity.ok("Successfully registered new user " + registerRequestDto.getEmail());
    }

    @Override
    public ResponseEntity<String> confirmRegistration(String token) {
        Optional<ConfirmationToken> confirmToken = confirmationTokenService.findConfirmationTokenByToken(token);
        if (confirmToken.isPresent()) {
            userService.confirmUser(confirmToken.get());
            return ResponseEntity.ok("User " + confirmToken.get().getUser().getEmail() + " confirmed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not registered or already confirmed");
        }
    }
}
