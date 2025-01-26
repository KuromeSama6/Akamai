package moe.ku6.akamai.data.akamai.session;

import lombok.*;
import moe.ku6.akamai.data.akamai.account.Account;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("session")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {
    private String id;
    @DBRef
    private Account account;
    private DateTime started;
    @Indexed(expireAfterSeconds = 0)
    @Setter
    private DateTime expire;

    public Session(SessionIssueRequest req) {
        if (req.getId().isEmpty()) throw new IllegalArgumentException("Request id cannot be empty.");
        id = req.getId();
        account = req.getAccount();
        started = DateTime.now();
    }
}
