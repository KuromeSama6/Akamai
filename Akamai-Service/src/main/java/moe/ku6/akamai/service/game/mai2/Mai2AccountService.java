package moe.ku6.akamai.service.game.mai2;

import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.game.ALLNetGameAccountRepository;
import moe.ku6.akamai.data.game.mai2.Mai2Account;
import moe.ku6.akamai.data.game.mai2.Mai2AccountRepo;
import moe.ku6.akamai.data.game.mai2.Mai2PlayLog;
import moe.ku6.akamai.data.sega.aimedb.card.AimeCard;
import moe.ku6.akamai.exception.api.NotFoundException;
import moe.ku6.akamai.service.game.ALLNetGameAccountService;
import moe.ku6.akamai.util.JsonWrapper;
import moe.ku6.akamai.util.RandomStringGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class Mai2AccountService extends ALLNetGameAccountService<Mai2Account> {
    @Autowired
    private Mai2AccountRepo accountRepo;

    @Override
    protected ALLNetGameAccountRepository<Mai2Account> GetAccountRepo() {
        return accountRepo;
    }

    @Override
    public Mai2Account CreateAccountAndCommit(AimeCard card) {
        var account = Mai2Account.builder();

        // general data
        account.id(RandomStringGenerator.GenerateRandomSnowflake())
                .username(RandomStringGenerator.GenerateRandomString(8))
                .aimeId(card.getAimeId());

        var ret = account.build();
        accountRepo.save(ret);
        return ret;
    }

    public Mai2PlayLog CreatePlayLog(Mai2Account account, JsonWrapper data) {
        var builder = Mai2PlayLog.builder()
                .id(RandomStringGenerator.GenerateRandomSnowflake())
                .account(account)
                .fullData(data)
                .date(DateTime.now());

        builder.versionRaw(data.GetInt("version"));

        builder.musicId(data.GetInt("musicId"))
                .trackNumber(data.GetInt("trackNo"))
                .dxScore(data.GetInt("deluxscore"))
                .dxScoreNewRecord(data.GetBool("isDeluxscoreNewRecord"))
                .newRecord(data.GetBool("isAchieveNewRecord"))
                .fastCount(data.GetInt("fastCount"))
                .lateCount(data.GetInt("lateCount"))
                .maxCombo(data.GetInt("maxCombo"))
                .totalCombo(data.GetInt("totalCombo"))
                .ratingBefore(data.GetInt("beforeRating"))
                .ratingAfter(data.GetInt("afterRating"))
                .trackCleared(data.GetBool("isClear"));

        return builder.build();
    }

    public List<JsonWrapper> GetUserDataListOrThrow(int aimeId, String section) {
        var user = accountRepo.FindByAimeId(aimeId);
        if (user == null)
            throw new NotFoundException();

        if (!user.getFullData().Has(section))
            throw new IllegalArgumentException("Section not found: %s".formatted(section));

        return user.getFullData().GetObjectList(section);
    }
}
