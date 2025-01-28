package moe.ku6.akamai.data.sega.aimedb.card;

import lombok.*;
import moe.ku6.akamai.data.akamai.account.AkamaiAccount;
import moe.ku6.akamai.util.RandomStringGenerator;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sega_aimedb_card")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AimeCard {
    @Id
    private String id;
    private int aimeId;
    private String accessCode;
    private DateTime lastAccess;
    private DateTime created;
    private String ipCreated;
    private String ipLastAccess;
    @DBRef
    private AkamaiAccount owner;

    public AimeCard(int aimeId, String accessCode, String ip) {
        id = RandomStringGenerator.GenerateRandomSnowflake();
        this.aimeId = aimeId;
        this.accessCode = accessCode;
        lastAccess = DateTime.now();
        created = DateTime.now();
        ipCreated = ip;
        ipLastAccess = ip;
    }
}
