package domostroy.auth.dto;

import java.util.Date;

public record JWTAuthenticationResponse(
        String token,
        Date expiresAt
) {
}
