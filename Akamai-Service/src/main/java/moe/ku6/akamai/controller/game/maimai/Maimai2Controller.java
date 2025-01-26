package moe.ku6.akamai.controller.game.maimai;

import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.sega.RawJson;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/g/mai2/Maimai2Servlet", consumes = "application/json", produces = "application/json")
@RawJson
public class Maimai2Controller {
    @PostMapping("/GetGameSettingApi")
    private JsonWrapper GetGameSettingsApi() {
        log.info("get ");
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
}
