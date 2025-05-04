package domostroy.auth.dto;

import jakarta.validation.constraints.*;

public record SignUpRequest(
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
        String email,
        @NotNull
        @Size(min = 8, max = 20)
        String password,

        @NotBlank
        @NotEmpty
        String firstName,

        String lastName,
        @Pattern(
                regexp = "^(\\+7|7|8)?\\s?\\(?\\d{3}\\)?\\s?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$",
                message = "Invalid Russian phone number"
        )
        @Size(min =8 , max = 16)
        String phoneNumber
) {
}

