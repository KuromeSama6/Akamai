package moe.ku6.akamai.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.akamai.AuthenticationOptional;
import moe.ku6.akamai.data.akamai.account.Account;
import moe.ku6.akamai.data.akamai.session.Session;
import moe.ku6.akamai.exception.api.NotAuthorizedException;
import moe.ku6.akamai.service.akamai.AccountSessionService;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Collections;

@Component
@Slf4j
public class UserSessionResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        var type = parameter.getParameterType();
        return type.equals(Account.class) || type.equals(Session.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        // bearer token
        var token = request.getHeader("Authorization").substring(7);
        var ret = AccountSessionService.getInstance().Validate(token);

        if (parameter.hasParameterAnnotation(AuthenticationOptional.class) || parameter.hasMethodAnnotation(AuthenticationOptional.class)) return ret;

        if (ret == null) throw new NotAuthorizedException();

        if (parameter.getParameterType() == Account.class) return ret.getAccount();
        else if (parameter.getParameterType() == Session.class) return ret;

        throw new IllegalArgumentException("Paramter type %s not supported".formatted(parameter.getParameterType()));
    }
}
