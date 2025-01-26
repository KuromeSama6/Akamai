package moe.ku6.akamai.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.api.UrlEncodedResponse;
import moe.ku6.akamai.annotation.sega.RawJson;
import moe.ku6.akamai.response.StandardAPIResponse;
import moe.ku6.akamai.util.JsonWrapper;
import moe.ku6.akamai.util.sega.ZLib;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JsonWrapperReturnResolver implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getParameterType().equals(JsonWrapper.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        JsonWrapper jsonWrapper = returnValue == null ? new JsonWrapper() : (JsonWrapper)returnValue;
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        if (response != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (parameter.hasMethodAnnotation(UrlEncodedResponse.class)) {
                StringBuilder res = new StringBuilder();
                for (var key : jsonWrapper.GetKeys()) {
                    res.append(key)
                            .append("=")
                            .append(jsonWrapper.Get(key).asText())
                            .append("&");
                }
                res.deleteCharAt(res.length() - 1);

                // set encoding
                response.setContentType("text/plain");
                var encoding = webRequest.getParameter("encode");
                response.setCharacterEncoding(encoding == null ? "EUC-JP" : encoding);
                response.setStatus(200);


                response.getOutputStream().write((res + "\n").getBytes(StandardCharsets.UTF_8));

            } else if (parameter.hasMethodAnnotation(RawJson.class) || parameter.getDeclaringClass().isAnnotationPresent(RawJson.class)) {
                log.info("return resolve");
                if ("deflate".equalsIgnoreCase(webRequest.getHeader("Content-Encoding"))) {
                    var ret = ZLib.Compress(jsonWrapper.toString().getBytes(StandardCharsets.UTF_8));
                    response.setHeader("Content-Encoding", "deflate");
                    response.getOutputStream().write(ret);

                } else {
                    response.getOutputStream().write(jsonWrapper.toString().getBytes(StandardCharsets.UTF_8));
                }
//                response.getOutputStream().flush();

            } else {
                new ObjectMapper().writeValue(response.getOutputStream(), StandardAPIResponse.ok().data(jsonWrapper.getJson()));
            }

            mavContainer.setRequestHandled(true);
        }
    }
}
