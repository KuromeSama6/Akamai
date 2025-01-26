package moe.ku6.akamai.data.sega.allnet.keychip;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import moe.ku6.akamai.data.akamai.account.Account;
import moe.ku6.akamai.util.CUID;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("keychip_session")
@Data
@Builder
@Getter
public class KeychipSession {
    @Id
    private String id;
    private String keychip;
    private String gameId;
    @DBRef
    private Account account;
    @Indexed(expireAfterSeconds = 0)
    private DateTime expire;
}
