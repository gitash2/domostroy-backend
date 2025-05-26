package domostroy.events.mail;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RentRequestStatusChangedEvent(
        @JsonProperty("email") String email,
        @JsonProperty("title") String title,
        @JsonProperty("changedStatus") String changedStatus
) {
}
