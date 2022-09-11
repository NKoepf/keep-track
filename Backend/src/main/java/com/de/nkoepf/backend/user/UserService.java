package com.de.nkoepf.backend.user;

import com.de.nkoepf.backend.mail.EmailSenderService;
import com.de.nkoepf.backend.token.ConfirmationToken;
import com.de.nkoepf.backend.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<StorageUser> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }
    }

    /***
     * Register new User in System
     * @param user will be registered
     * @throws IllegalArgumentException if the given user is already in the system
     */
    void signUpUser(StorageUser user) throws IllegalArgumentException {
        Optional<StorageUser> storageUser = userRepository.findByEmail(user.getEmail());
        if (storageUser.isPresent()) {
            if (storageUser.get().getEnabled()) {
                throw new IllegalArgumentException();
            } else {
                Optional<ConfirmationToken> token = confirmationTokenService.findConfirmationTokenByUser(user);
                sendConfirmationMail(user.getEmail(), token.get().getConfirmationToken());
            }
        }

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        final StorageUser createdUser = userRepository.save(user);
        final ConfirmationToken confirmationToken = new ConfirmationToken(createdUser);

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
    }

    void confirmUser(ConfirmationToken confirmationToken) {
        final StorageUser user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }

    void sendConfirmationMail(String userMail, String token) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("Mail Confirmation Link!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText(
                "Thank you for registering. Please click on the below link to activate your account." + "http://localhost:8001/api/sign-up/confirm?token="
                        + token);
        emailSenderService.sendEmail(mailMessage);
    }

    /***
     * Checks if token of request is still valid
     * @param token contains credentials of token request. Checks if current token is still valid
     * @return token with authenticated flag, according to authentication status of user
     */
    public Authentication isAuthenticated(UsernamePasswordAuthenticationToken token) {
        Optional<StorageUser> user = userRepository.findByEmail(token.getPrincipal().toString());
        if (user.isEmpty()
                || !user.get().getEmail().equals(token.getPrincipal().toString())
                || !bCryptPasswordEncoder.matches(token.getCredentials().toString(), user.get().getPassword())
                || !Boolean.TRUE.equals(user.get().getEnabled())) {
            token.setAuthenticated(false);
        }
        return token;
    }
}
