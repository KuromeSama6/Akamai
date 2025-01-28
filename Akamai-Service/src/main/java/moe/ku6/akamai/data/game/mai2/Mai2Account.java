package moe.ku6.akamai.data.game.mai2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.game.ALLNetGameAccount;
import moe.ku6.akamai.util.JsonWrapper;
import moe.ku6.akamai.util.Util;
import org.springframework.data.mongodb.core.mapping.Document;

@Slf4j
@Document("sega_game_mai2_account")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Mai2Account extends ALLNetGameAccount {
    private String username;
    private int rating;
    private JsonWrapper fullData = new JsonWrapper();

    public JsonWrapper GetPreviewData() {
        var lastSession = getLastPlay();

        return new JsonWrapper()
                .Set("userId", getAimeId())
                .Set("userName", username)
                .Set("isLogin", false)

                .Set("lastGameId", lastSession.getGameId())
                .Set("lastDataVersion", lastSession.getDataVersion())
                .Set("lastRomVersion", lastSession.getRomVersion())
                .Set("lastLoginDate", Util.FormatDatetimeALLNetUTC(lastSession.getDate()))
                .Set("lastPlayDate", Util.FormatDatetimeALLNetUTC(lastSession.getDate()))

                .Set("playerRating", rating)

                .Set("nameplateId", fullData.GetInt("userData.plateId", 1))
                .Set("iconId", fullData.GetInt("userData.iconId", 1))
                .Set("trophyId", 0)
                .Set("partnerId", fullData.GetInt("userData.partnerId", 1))
                .Set("frameId", fullData.GetInt("userData.frameId", 1))

                .Set("totalAwake", 0)
                .Set("isNetMember", 1)

                .Set("dailyBonusDate", "1970-01-01 09:00:00.0")

                .Set("headPhoneVolume", 0)

                .Set("dispRate", 0)
                .Set("isInherit", false)
                .Set("banState", 0);
    }

    public void UpdateFullData(JsonWrapper data) {
        fullData = data;
        var userData = GetDataSectionSingle("userData");

        username = userData.GetString("userName", username);
        rating = userData.GetInt("playerRating", rating);
    }

    public JsonWrapper GetDataSectionSingle(String section) {
        return fullData.GetObjectList(section).getFirst();
    }
}
