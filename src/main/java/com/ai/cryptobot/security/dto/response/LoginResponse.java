package com.ai.cryptobot.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ADMIN
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String fullName;
    private String email;
    private boolean active;
    private List<String> roles;
    private String token;
    private String refreshToken;
}
