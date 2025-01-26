package moe.ku6.akamai.advice;

import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.exception.sega.allnet.ALLNetException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@Slf4j
@Order(1)
public class ALLNetApiExceptionHandler {
    @ExceptionHandler(ALLNetException.class)
    public ResponseEntity<?> OnALLNetException(ALLNetException e) {
        return ResponseEntity
                .status(400)
                .body("ALLNet error: %s".formatted(e.getMessage()));
    }

}
