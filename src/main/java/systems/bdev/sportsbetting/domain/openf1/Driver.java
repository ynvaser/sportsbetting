package systems.bdev.sportsbetting.domain.openf1;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Driver(
        @JsonProperty("broadcast_name") String broadcastName,
        @JsonProperty("country_code") String countryCode,
        @JsonProperty("driver_number") int driverNumber,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("headshot_url") String headshotUrl,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("meeting_key") int meetingKey,
        @JsonProperty("name_acronym") String nameAcronym,
        @JsonProperty("session_key") int sessionKey,
        @JsonProperty("team_colour") String teamColour,
        @JsonProperty("team_name") String teamName
) {
}
