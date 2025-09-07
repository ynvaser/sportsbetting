package systems.bdev.sportsbetting.domain.openf1;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Session(
        @JsonProperty("circuit_key") int circuitKey,
        @JsonProperty("circuit_short_name") String circuitShortName,
        @JsonProperty("country_code") String countryCode,
        @JsonProperty("country_key") int countryKey,
        @JsonProperty("country_name") String countryName,
        @JsonProperty("date_end") String dateEnd,
        @JsonProperty("date_start") String dateStart,
        @JsonProperty("gmt_offset") String gmtOffset,
        @JsonProperty("location") String location,
        @JsonProperty("meeting_key") int meetingKey,
        @JsonProperty("session_key") int sessionKey,
        @JsonProperty("session_name") String sessionName,
        @JsonProperty("session_type") String sessionType,
        @JsonProperty("year") int year
) {
}
