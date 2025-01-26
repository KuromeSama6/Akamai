package moe.ku6.akamai.data.akamai.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import moe.ku6.akamai.service.akamai.AccountService;
import moe.ku6.akamai.util.IJsonSerializable;
import moe.ku6.akamai.util.JsonWrapper;
import moe.ku6.akamai.util.RandomStringGenerator;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("akamai_account")
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Account implements IJsonSerializable {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    @Setter
    private String hwid;
    private AccountStatus status = AccountStatus.OK;
    private DateTime created;

    @Setter
    private String keychip;

    public Account(String username, String email, String password) {
        this.id = RandomStringGenerator.GenerateRandomSnowflake();
        this.username = username;
        this.password = password;
        this.email = email;
        created = DateTime.now();
        status = AccountStatus.AWAITING_ACTIVATION;
        keychip = AccountService.getInstance().AllocateKeychipId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account)o;
        return id.equals(account.id);
    }

    @Override
    public void SerializeInternal(JsonWrapper json) {
        json.Set("id", id);
        json.Set("username", username);
        json.Set("status", status.toString());
        json.Set("email", email);
    }
}
