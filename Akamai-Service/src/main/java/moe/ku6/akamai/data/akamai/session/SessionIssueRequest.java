package moe.ku6.akamai.data.akamai.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import moe.ku6.akamai.data.akamai.account.AkamaiAccount;

@Builder
@Getter
public class SessionIssueRequest {
    private AkamaiAccount account;
    private String hwid;
    @Setter
    private String id;
}
