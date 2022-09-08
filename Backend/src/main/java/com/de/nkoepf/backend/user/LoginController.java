package com.de.nkoepf.backend.user;

import com.de.nkoepf.backend.auth.JwtUtil;
import com.de.nkoepf.backend.token.ConfirmationToken;
import com.de.nkoepf.backend.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtUtil jwtUtil;

    @GetMapping("/sign-in")
    String signIn(Model model) {
        model.addAttribute("user", new StorageUser());
        return "sign-in";
    }

    @GetMapping("/sign-up")
    String signUp(Model model) {
        model.addAttribute("user", new StorageUser());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    String signUp(StorageUser user) {

        userService.signUpUser(user);

        return "redirect:/confirm";
    }

    @GetMapping("/sign-up/confirm")
    String confirmMail(@RequestParam("token") String token) {

        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);
        optionalConfirmationToken.ifPresent(userService::confirmUser);
        return "/sign-in";
    }

}
