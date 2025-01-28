package moe.ku6.akamai.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.game.RequireGameSession;
import moe.ku6.akamai.exception.api.NotAuthorizedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ALLNetGameSessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            if (handlerMethod.hasMethodAnnotation(RequireGameSession.class)) {
                log.info("Game session required for {}", handlerMethod.getMethod().getName());
                throw new NotAuthorizedException();
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
