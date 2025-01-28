package moe.ku6.akamai.service.sega.allnet;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.akamai.account.AkamaiAccount;
import moe.ku6.akamai.data.sega.allnet.keychip.KeychipSession;
import moe.ku6.akamai.data.sega.allnet.keychip.KeychipSessionRepo;
import moe.ku6.akamai.util.RandomStringGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeychipService {
    @Getter
    private static KeychipService instance;

    @Value("${akamai.sega.allnet.keychip.expire}")
    private int keychipExpireSeconds;
    @Autowired
    @Getter
    private KeychipSessionRepo sessionRepo;

    public KeychipService() {
        instance = this;
    }

    public KeychipSession CreateSession(AkamaiAccount account, String gameId) {
        var session = KeychipSession.builder()
                .id(RandomStringGenerator.GenerateRandomSnowflake())
                .keychip(account.getKeychip())
                .account(account)
                .gameId(gameId)
                .expire(DateTime.now().plusSeconds(keychipExpireSeconds))
                .build();

        // invalidate old session
        sessionRepo.DeleteByKeychipAndGameId(account.getKeychip(), gameId);
        sessionRepo.save(session);

        return session;
    }

}
