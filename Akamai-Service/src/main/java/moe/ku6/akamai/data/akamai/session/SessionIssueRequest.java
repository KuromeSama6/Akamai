package moe.ku6.akamai.data.akamai.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import moe.ku6.akamai.data.akamai.account.Account;

@Builder
@Getter
public class SessionIssueRequest {
    private Account account;
    private String hwid;
    @Setter
    private String id;
}
