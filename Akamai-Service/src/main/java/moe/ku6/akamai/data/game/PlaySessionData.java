package moe.ku6.akamai.data.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

/**
 * Holds information about a play session.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PlaySessionData {
    private String gameId;
    private String romVersion;
    private String dataVersion;
    private DateTime date;
    private int placeId;
    private String placeName;
    private String regionName;
    private String clientId;
    private String countryCode;
    private String ip;
}
