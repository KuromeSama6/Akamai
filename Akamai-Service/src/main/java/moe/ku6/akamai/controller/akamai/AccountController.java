package moe.ku6.akamai.controller.akamai;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.akamai.session.Session;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/account", consumes = "application/json", produces = "application/json")
public class AccountController {
    @GetMapping(value = "/info", consumes = "*/*")
    private JsonWrapper GetAccountInfo(Session session) {
        return new JsonWrapper()
                .Set("account", session.getAccount());
    }

    @GetMapping(value = "/keychip")
    private JsonWrapper GetKeychip(Session session) {
        log.info("Updating keychip for account {}", session.getAccount().getId());

        return null;
    }
}
