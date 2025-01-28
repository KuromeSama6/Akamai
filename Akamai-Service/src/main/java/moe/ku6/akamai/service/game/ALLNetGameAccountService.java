package moe.ku6.akamai.service.game;

import moe.ku6.akamai.data.game.ALLNetGameAccount;
import moe.ku6.akamai.data.game.ALLNetGameAccountRepository;
import moe.ku6.akamai.data.game.PlaySessionData;
import moe.ku6.akamai.data.game.mai2.Mai2Account;
import moe.ku6.akamai.data.sega.aimedb.card.AimeCard;
import moe.ku6.akamai.data.sega.allnet.keychip.KeychipSession;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class ALLNetGameAccountService<T extends ALLNetGameAccount> {
    protected abstract ALLNetGameAccountRepository<T> GetAccountRepo();
    public abstract T CreateAccountAndCommit(AimeCard card);

    public PlaySessionData CreatePlaySession(
            T account,
            KeychipSession keychip,
            String dataVersion,
            String romVersion,
            String ip
    ) {
        var playSession = PlaySessionData.builder()
                .date(DateTime.now())
                .countryCode("JPN")
                .dataVersion(dataVersion)
                .gameId(keychip.getGameId())
                .placeId(keychip.getPlaceId())
                .clientId(keychip.getKeychip())
                .placeName("")
                .regionName("W")
                .romVersion(romVersion)
                .dataVersion(dataVersion)
                .ip(ip)
                .build();
        return playSession;
    }

    public void RecordAccountLogin(
            T account,
            KeychipSession keychip,
            String dataVersion,
            String romVersion,
            String ip
    ) {
        var playSession = CreatePlaySession(account, keychip, dataVersion, romVersion, ip);
        if (account.getFirstPlay() == null) account.setFirstPlay(playSession);
        account.setLastPlay(playSession);

        GetAccountRepo().save(account);
    }
}
