package moe.ku6.akamai.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.response.StandardAPIResponse;
import moe.ku6.akamai.util.JsonWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(0)
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();

            response.setStatus(500);
            response.setContentType("application/json");
            new ObjectMapper().writeValue(response.getOutputStream(), new StandardAPIResponse(1, "server error"));
        }
    }
}
