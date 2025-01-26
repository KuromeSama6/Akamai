package moe.ku6.akamai.service.akamai;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.akamai.session.SessionIssueRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@Getter
public class JWTService {
    @Value("${akamai.web.jwt.secret}")
    private String secret;
    @Value("${akamai.web.jwt.issuer}")
    private String jwtIssuer;

    public String IssueAccountToken(SessionIssueRequest req) {
        var builder = Jwts.builder()
                .signWith(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"))
                .subject(req.getAccount().getId())
                .issuedAt(DateTime.now().toDate())
                .issuer(jwtIssuer)
                .id(req.getId());


        return builder.compact();
    }

    public Jws<Claims> Validate(String token, String key) {
        var parser = Jwts.parser()
                .verifyWith(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"))
                .build();

        try {
            var ret = parser.parseSignedClaims(token);
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

}
