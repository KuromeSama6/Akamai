package moe.ku6.akamai.controller.game.maimai;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.sega.RawJson;
import moe.ku6.akamai.response.StandardAPIResponse;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

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
                .Set("apiName", "com.sega.maimai2servlet.api." + req.getRequestURI().replace("/ext/game/mai2/Maimai2Servlet/", ""));
    }
}
