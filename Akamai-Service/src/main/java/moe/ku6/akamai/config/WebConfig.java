package moe.ku6.akamai.config;

import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.interceptor.ALLNetGameSessionInterceptor;
import moe.ku6.akamai.resolver.JsonWrapperBodyResolver;
import moe.ku6.akamai.resolver.JsonWrapperReturnResolver;
import moe.ku6.akamai.resolver.UserSessionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Slf4j
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ALLNetGameSessionInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new DFIEncodedArgumentResolver());
        resolvers.add(new JsonWrapperBodyResolver());
        resolvers.add(new UserSessionResolver());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new JsonWrapperReturnResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
}
