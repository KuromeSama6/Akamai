package moe.ku6.akamai.service.akamai;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.akamai.session.Session;
import moe.ku6.akamai.data.akamai.session.SessionIssueRequest;
import moe.ku6.akamai.data.akamai.session.SessionRepo;
import moe.ku6.akamai.util.RandomStringGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AccountSessionService {
    @Getter
    private static AccountSessionService instance;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private SessionRepo sessionRepo;

    @PostConstruct
    private void Init() {
        instance = this;
    }

    public String CreateSession(SessionIssueRequest req) {
        // remove existing
        sessionRepo.DeleteByAccount(req.getAccount().getId());

        req.setId(RandomStringGenerator.GenerateRandomSnowflake());
        Session session = new Session(req);
        session.setExpire(DateTime.now().plusDays(7));
        sessionRepo.save(session);

        return jwtService.IssueAccountToken(req);
    }

    public Session Validate(String token) {
        var claims = jwtService.Validate(token, jwtService.getSecret());
        if (claims == null) return null;

        String id = claims.getPayload().getId();

        var session = GetSession(id);
        if (session == null || session.getAccount() == null) return null;
        return session;
    }

    private Session GetSession(String id) {
        var ret = sessionRepo.findById(id).orElse(null);
        return ret;
    }

    public void DeleteSession(Session session) {
        sessionRepo.delete(session);
    }
}
