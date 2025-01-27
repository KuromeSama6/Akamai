package moe.ku6.akamai.controller.game.maimai2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.sega.RawJson;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = Maimai2Controller.class)
@Order(5)
public class Maimai2ControllerAdvice {
    @ExceptionHandler(Exception.class)
    @RawJson
    public JsonWrapper OnException(Exception e, HttpServletRequest req) {
        log.error("Exception in Maimai2Controller", e);
        return new JsonWrapper()
                .Set("returnCode", 1)
                .Set("apiName", "com.sega.maimai2servlet.api." + req.getRequestURI().replace("/g/mai2/Maimai2Servlet/", ""));
    }
}
