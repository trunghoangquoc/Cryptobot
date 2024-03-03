package com.ai.cryptobot.security.filter;

import com.ai.cryptobot.security.MemberCustom;
import com.ai.cryptobot.security.dto.ClaimsToken;
import com.ai.cryptobot.security.dto.response.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "APPLICATION")
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${application.jwt.secret.key}")
    private String APPLICATION_JWT_SECRET_KEY;

    @Value("${application.expiration.time}")
    private long APPLICATION_JWT_EXPIRATION_TIME;

    @Value("${application.expiration.refresh.time}")
    private long APPLICATION_JWT_EXPIRATION_REFRESH_TIME;

    private final ObjectMapper objectMapper;

    public LoginResponse generateToken(Authentication authentication) {
        MemberCustom principal = (MemberCustom) authentication.getPrincipal();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(generateTokenByAuthentication(principal));
        loginResponse.setRefreshToken(generateTokenByAuthentication(principal, APPLICATION_JWT_EXPIRATION_REFRESH_TIME));
        return loginResponse;
    }

    private String generateTokenByAuthentication(MemberCustom principal) {
        return generateTokenByAuthentication(principal, APPLICATION_JWT_EXPIRATION_TIME);
    }

    private String generateTokenByAuthentication(MemberCustom principal, long expiredTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiredTime);

        ClaimsToken model = new ClaimsToken(principal);
        for (GrantedAuthority authority : principal.getAuthorities()) {
            model.getRoles().add(authority.getAuthority());
        }
        String subject = principal.getUsername();
        Map<String, Object> claims = new HashMap<>();
        claims.put("model", model);
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, APPLICATION_JWT_SECRET_KEY)
                .compact();
    }

    public ClaimsToken getInfoFromJWT(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(APPLICATION_JWT_SECRET_KEY).parseClaimsJws(token).getBody();
            return objectMapper.convertValue(claims.get("model"), ClaimsToken.class);
        } catch (SignatureException e) {
            log.error("Invalid JWT Signature");
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT Token");
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token");
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT Claims string is empty.");
            throw e;
        }
    }

}
