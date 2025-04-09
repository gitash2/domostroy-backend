package domostroy.events.mail;


import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegisteredEvent(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password,
        @JsonProperty("confirmationCode") String confirmationCode
) {

}
