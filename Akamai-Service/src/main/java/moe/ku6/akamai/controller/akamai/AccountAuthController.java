package moe.ku6.akamai.controller.akamai;

import jakarta.validation.Valid;
import moe.ku6.akamai.data.akamai.account.Account;
import moe.ku6.akamai.data.akamai.account.AccountRepo;
import moe.ku6.akamai.data.akamai.session.SessionIssueRequest;
import moe.ku6.akamai.exception.api.APIException;
import moe.ku6.akamai.exception.api.FeatureNotImplementedException;
import moe.ku6.akamai.request.auth.RegisterRequest;
import moe.ku6.akamai.request.auth.WebAuthenticateRequest;
import moe.ku6.akamai.service.akamai.AccountSessionService;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", consumes = "application/json", produces = "application/json")
public class AccountAuthController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountSessionService sessionService;

    @PostMapping("/authenticate")
    public JsonWrapper Authenticate(@RequestBody @Valid WebAuthenticateRequest req) {
        var account = accountRepo.FindByUsername(req.getUsername());
        if (account == null)
            throw new APIException(401, 16, "invalid credentials");

        if (!passwordEncoder.matches(req.getPassword(), account.getPassword()))
            throw new APIException(401, 16, "invalid credentials");

        var issueRequest = SessionIssueRequest.builder()
                .account(account)
                .build();
        var token = sessionService.CreateSession(issueRequest);

        return new JsonWrapper()
                .Set("token", token)
                .Set("id", account.getId())
                .Set("account", account);
    }

    @PostMapping("/register")
    public JsonWrapper Register(@RequestBody @Valid RegisterRequest req) {
        if (accountRepo.FindByUsernameOrEmail(req.getUsername(), req.getEmail()) != null)
            throw new APIException(409, "username/email unavailable");

        String passwordHash = passwordEncoder.encode(req.getPassword());
        Account account = new Account(req.getUsername(), req.getEmail(), passwordHash);

        accountRepo.save(account);
        return null;
    }
}
