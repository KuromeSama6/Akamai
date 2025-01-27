package moe.ku6.akamai.data.sega.aimedb.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import moe.ku6.akamai.data.akamai.account.Account;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sega_aime_card")
@Data
@Builder
@AllArgsConstructor
public class AimeCard {
    @Id
    private String id;
    private String accessCode;
    private DateTime lastAccess;
    private DateTime created;
    private String ipCreated;
    private String ipLastAccess;
    @DBRef
    private Account owner;
}
