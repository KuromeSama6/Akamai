package moe.ku6.akamai.service.sega.aimedb;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.sega.aimedb.card.AimeCard;
import moe.ku6.akamai.data.sega.aimedb.card.AimeCardRepo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AimeCardService {
    @Getter
    private static AimeCardService instance;
    @Autowired
    @Getter
    private AimeCardRepo cardRepo;

    public AimeCardService() {
        instance = this;
    }

    /**
     * Gets an AIME card by the provided access code and logs this access attempt.
     * @param accessCode The access code of the AIME card.
     * @return The AIME card with the provided access code, or null if no card was found.
     */
    public AimeCard GetAndAccess(String accessCode, String ipAddress) {
        var card = cardRepo.FindByAccessCode(accessCode);
        if (card == null) return null;

        card.setLastAccess(DateTime.now());
        card.setIpLastAccess(ipAddress);
        cardRepo.save(card);

        return card;
    }

}
