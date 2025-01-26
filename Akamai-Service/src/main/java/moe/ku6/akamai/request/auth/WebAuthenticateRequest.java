package moe.ku6.akamai.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebAuthenticateRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{1,16}$")
    private String username;
    @NotBlank
    @Pattern(regexp = "^[\\x00-\\x7F]{1,64}$")
    private String password;
}
