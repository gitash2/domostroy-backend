package domostroy.auth.api;

import domostroy.auth.constants.Constants;
import domostroy.auth.dto.ConfirmRequest;
import domostroy.auth.dto.JWTAuthenticationResponse;
import domostroy.auth.dto.SignInRequest;
import domostroy.auth.dto.SignUpRequest;
import domostroy.auth.security.service.AuthenticationService;
import domostroy.auth.users.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Hidden
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader(name = Constants.AUTH_HEADER_KEY) String token) {
        boolean res = authenticationService.validateToken(token);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Validated SignUpRequest req) {
        userService.signUp(req);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JWTAuthenticationResponse> signIn(@RequestBody @Validated SignInRequest req) {
        return ResponseEntity.ok(authenticationService.signIn(req));
    }

    @PostMapping("/confirmRegistration")
    public void confirmRegistration(@RequestBody ConfirmRequest req) {
        userService.confirmRegistration(req);
    }


}
