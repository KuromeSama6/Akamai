package moe.ku6.akamai.beans;

import org.springframework.context.annotation.Bean;

import java.security.SecureRandom;

public class CommonBeans {
    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
