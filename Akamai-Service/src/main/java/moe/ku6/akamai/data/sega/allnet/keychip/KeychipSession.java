package moe.ku6.akamai.data.sega.allnet.keychip;

import lombok.*;
import moe.ku6.akamai.data.akamai.account.AkamaiAccount;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sega_aimedb_keychip_session")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeychipSession {
    @Id
    private String id;
    private String keychip;
    private String gameId;
    private int placeId;
    @DBRef
    private AkamaiAccount account;
    @Indexed(expireAfterSeconds = 0)
    private DateTime expire;
}
