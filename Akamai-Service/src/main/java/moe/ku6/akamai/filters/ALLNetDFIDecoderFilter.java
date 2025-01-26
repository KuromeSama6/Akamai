package moe.ku6.akamai.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.util.JsonWrapper;
import moe.ku6.akamai.util.sega.ALLNetDecoder;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ALLNetDFIDecoderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader("Pragma");

        if (header == null || !header.equalsIgnoreCase("DFI")) {
            filterChain.doFilter(request, response);
            return;
        }

        var decoded = ALLNetDecoder.DecodeDFI(request.getInputStream().readAllBytes(), true, false);
        filterChain.doFilter(new DecodedRequest(request, ParseParameters(new String(decoded, StandardCharsets.UTF_8).replace("\r\n", ""))), response);
    }

    private static class DecodedRequest extends HttpServletRequestWrapper {
        private final Map<String, String[]> content;

        public DecodedRequest(HttpServletRequest request, Map<String, String[]> content) {
            super(request);
            this.content = content;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return content;
        }

        @Override
        public String getParameter(String name) {
            return content.get(name) != null ? content.get(name)[0] : null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(content.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return content.get(name);
        }
    }

    private static Map<String, String[]> ParseParameters(String decodedString) {
        Map<String, String[]> ret = new HashMap<>();
        if (decodedString != null && !decodedString.isEmpty()) {
            String[] pairs = decodedString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    if (ret.containsKey(key)) {
                        ArrayList<String> values = new ArrayList<>(List.of(ret.get(key)));
                        values.add(value);
                        ret.put(key, values.toArray(new String[0]));
                    } else {
                        ret.put(key, new String[]{value});
                    }
                }
            }
        }

        return ret;
    }
}
