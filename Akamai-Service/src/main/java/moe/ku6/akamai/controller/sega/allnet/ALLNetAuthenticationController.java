package moe.ku6.akamai.controller.sega.allnet;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.api.UrlEncodedResponse;
import moe.ku6.akamai.data.akamai.account.AccountRepo;
import moe.ku6.akamai.exception.sega.allnet.ALLNetException;
import moe.ku6.akamai.service.sega.allnet.KeychipSessionService;
import moe.ku6.akamai.util.JsonWrapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/sys/servlet", consumes = "application/x-www-form-urlencoded", produces = "text/plain")
public class ALLNetAuthenticationController {
    @Value("${akamai.sega.allnet.props.place-name}")
    private String allnetLocationName;

    @Autowired
    private KeychipSessionService keychipSessionService;
    @Autowired
    private AccountRepo accountRepo;

    @PostMapping("/PowerOn")
    @UrlEncodedResponse
    private JsonWrapper CabinetPowerOn(HttpServletRequest req, @RequestParam Map<String, String> data) {
        log.info("CabinetPowerOn: {}", data);
        var localAddr = req.getLocalAddr();
        var localPort = String.valueOf(req.getLocalPort());
        // CabinetPowerOn: {encode=UTF-8, ver=0.00, serial=A4364672943, boot_ver=0000, ip=172.28.0.1, firm_ver=60001, format_ver=3, hops=1, game_id=SDGA, token=2210014277

        var serial = data.get("serial");
        if (serial == null || !serial.matches("^A[0-9]{10}$")) throw new ALLNetException("invalid serial");

        var account = accountRepo.FindByKeychip(serial);
        if (account == null) throw new ALLNetException("serial not registered");

        var gameId = data.get("game_id");
        if (gameId == null || gameId.isEmpty()) throw new ALLNetException("invalid game id");

        var token = data.get("token");
        if (token == null || token.isEmpty()) throw new ALLNetException("invalid token");

        var session = keychipSessionService.CreateSession(account, gameId);
        var version = data.getOrDefault("ver", "1.00");

        var ret = new JsonWrapper()
                .Set("stat", "1")
                .Set("name", "")
                .Set("place_id", "123")
                .Set("region0", "1")
                .Set("region_name0", "W")
                .Set("region_name1", "X")
                .Set("region_name2", "Y")
                .Set("region_name3", "Z")
                .Set("country", "JPN")
                .Set("nickname", allnetLocationName);

        // title server
        {
            ret.Set("uri", GetTitleServerUri(localAddr, localPort, gameId, version, session.getId()))
                .Set("host", localAddr);
        }

        var now = DateTime.now();
        if (version.startsWith("2")) {
            ret.Set("year", now.getYear())
                    .Set("month", now.getMonthOfYear())
                    .Set("day", now.getDayOfMonth())
                    .Set("hour", now.getHourOfDay())
                    .Set("minute", now.getMinuteOfHour())
                    .Set("second", now.getSecondOfMinute())
                    .Set("setting", 1)
                    .Set("timezone", "+09:00")
                    .Set("res_class", "PowerOnResponseV2");
        } else {
            ret
                    .Set("allnet_id", 456)
                    .Set("client_timezone", "+0900")
                    .Set("utc_time", now.toDateTime(DateTimeZone.UTC).toString().substring(0, 19) + "Z")
                    .Set("setting", "")
                    .Set("res_ver", 3)
                    .Set("token", token);
        }

        log.info(ret.toString());
        return ret;
    }

    private static String GetTitleServerUri(String localAddr, String localPort, String gameId, String version, String session) {

        var addr = localAddr + ":" + localPort;
        return "http://%s/g/".formatted(addr) + switch (gameId.toUpperCase()) {
            case "SDBT" -> "chu2/%s/%s/".formatted(version, session);
            case "SDHD" -> "chu3/%s/".formatted(version);
            case "SDGS" -> "chu3/%s/".formatted(version);
            case "SBZV" -> "diva/";
            case "SDDT" -> "ongeki/";
            case "SDEY" -> "mai/";
            case "SDGA" -> "mai2/";
            case "SDGB" -> "mai2/";
            case "SDEZ" -> "mai2/";
            case "SDFE" -> "wacca";
            case "SDED" -> "card/";

            default -> "";
        };
    }
}
