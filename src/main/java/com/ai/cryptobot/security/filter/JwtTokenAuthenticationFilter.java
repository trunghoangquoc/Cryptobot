package com.ai.cryptobot.security.filter;

import com.ai.cryptobot.common.Constant;
import com.ai.cryptobot.security.dto.ClaimsToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ADMIN
 */
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals(Constant.LOGIN_URL) || request.getServletPath().equals(Constant.REFRESH_TOKEN_URL)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(Constant.AUTHORIZATION);
            if (StringUtils.isNotBlank(authorizationHeader)) {
                String token = authorizationHeader.replace(Constant.BEARER, "").trim();
                if (StringUtils.isNotBlank(token)) {
                    try {
                        ClaimsToken model = getClaimsToken(token);
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        for (String role : model.getRoles()) {
                            authorities.add(new SimpleGrantedAuthority(role));
                        }
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(model.getUsername(), null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    } catch (Exception ex) {
                        log.error("Error logging : {}", ex.getMessage());
                        response.setHeader("error", ex.getMessage());
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        Map<String, String> error = new HashMap<>();
                        error.put("error_message", ex.getMessage());
                        response.setContentType(Constant.APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    }
                } else {
                    log.error("Error no token");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", "no token");
                    response.setContentType(Constant.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    private ClaimsToken getClaimsToken(String token) {
        try {
            return jwtTokenProvider.getInfoFromJWT(token);
        } catch (Exception e) {
            return null;
        }
    }

}
