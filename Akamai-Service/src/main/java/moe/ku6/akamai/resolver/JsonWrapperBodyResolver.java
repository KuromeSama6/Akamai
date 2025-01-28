package moe.ku6.akamai.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.util.JsonWrapper;
import moe.ku6.akamai.util.sega.ZLib;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class JsonWrapperBodyResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(JsonWrapper.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String body;
        if ("deflate".equalsIgnoreCase(webRequest.getHeader("Content-Encoding"))) {
            body = new String(ZLib.Decompress(webRequest.getNativeRequest(HttpServletRequest.class).getInputStream().readAllBytes()), StandardCharsets.UTF_8);

        } else {
            body = webRequest.getNativeRequest(HttpServletRequest.class).getReader().lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
        return new JsonWrapper(body);
    }
}
