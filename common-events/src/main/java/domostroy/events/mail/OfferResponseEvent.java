package domostroy.events.mail;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OfferResponseEvent(
        @JsonProperty("email") String email,
        @JsonProperty("title") String title
) {
}
