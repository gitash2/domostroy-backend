package domostroy.core.security;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BearerToken(
        @Pattern(regexp = "^Bearer .*$", message = "Некорректный формат токена")
        @Size(min = 10, message = "Некорректный размер токена")
        String rawToken
) implements SecurityToken {

    @Override
    public String getToken() {
        return rawToken.substring(Constants.DEFAULT_TOKEN_PREFIX.length());


    }
}
