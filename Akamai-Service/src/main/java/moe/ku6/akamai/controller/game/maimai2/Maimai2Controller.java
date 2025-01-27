package moe.ku6.akamai.controller.game.maimai2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.sega.RawJson;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping(value = "/g/mai2/Maimai2Servlet", consumes = "application/json", produces = "application/json")
@RawJson
public class Maimai2Controller {
    @PostMapping("/GetGameSettingApi")
    private JsonWrapper GetGameSettingsApi() {
        return new JsonWrapper()
                .Set("isAouAccession", true)
                .EnterObject()
                .Set("rebootStartTime", "2020-01-01 23:59:00.0")
                .Set("rebootEndTime", "2020-01-01 23:59:00.0")
                .Set("rebootInterval", 0)
                .Set("isMaintenance", false)
                .Set("requestInterval", 10)
                .Set("movieUploadLimit", 0)
                .Set("movieStatus", 0)
                .Set("movieServerUri", "")
                .Set("deliverServerUri", "")
                .Set("oldServerUri", "")
                .Set("usbDlServerUri", "")
                .Set("pingDisable", true)
                .Set("packetTimeout", 20000)
                .Set("packetTimeoutLong", 60000)
                .Set("packetRetryCount", 5)
                .Set("userDataDlErrTimeout", 300000)
                .Set("userDataDlErrRetryCount", 5)
                .Set("userDataDlErrSamePacketRetryCount", 5)
                .Set("userDataUpSkipTimeout", 0)
                .Set("userDataUpSkipRetryCount", 0)
                .Set("iconPhotoDisable", true)
                .Set("uploadPhotoDisable", false)
                .Set("maxCountMusic", 0)
                .Set("maxCountItem", 0)
                .ExitObject("gameSetting");
    }

    @PostMapping("/GetGameRankingApi")
    private JsonWrapper GetGameRankingApi() {
        return new JsonWrapper()
                .Set("type", 1)
                .Set("gameRankingList", new ArrayList<>());
    }

    @PostMapping("/GetGameEventApi")
    private JsonWrapper GetGameEventApi() {
        return new JsonWrapper()
                .Set("type", 1)
                .Set("gameEventList", new ArrayList<>());
    }

    /*
    Tournaments not supported at the moment
     */
    @PostMapping("/GetGameTournamentInfoApi")
    private JsonWrapper GetGameTournamentInfoApi() {
        var ret = new ArrayList<>();
        return new JsonWrapper()
                .Set("length", ret.size())
                .Set("gameTournamentInfoList", ret);
    }

    @PostMapping("/GetGameChargeApi")
    private JsonWrapper GetGameChargeApi() {
        var ret = new ArrayList<>();
        return new JsonWrapper()
                .Set("type", ret.size())
                .Set("gameChargeList", ret);
    }

    @PostMapping("/GetGameNgMusicIdApi")
    private JsonWrapper GetGameNgMusicIdApi() {
        var ret = new ArrayList<>();
        return new JsonWrapper()
                .Set("length", ret.size())
                .Set("gameNgMusicIdList", ret);
    }

    @PostMapping("/GetGameWeeklyDataApi")
    private JsonWrapper GetGameWeeklyDataApi() {
        return new JsonWrapper()
                .EnterObject()
                .Set("missionCategory", 0)
                .Set("updateDate", "2024-01-01 00:00:00.0")
                .Set("beforeDate", "2077-01-01 00:00:00.0")
                .ExitObject("gameWeeklyData");
    }

    @PostMapping("/{apiName}")
    private JsonWrapper NOPEndpoints(@PathVariable("apiName") String apiName) {
        return new JsonWrapper()
                .Set("returnCode", 1)
                .Set("api", "com.sega.maimai2servlet.api." + apiName);
    }

}
