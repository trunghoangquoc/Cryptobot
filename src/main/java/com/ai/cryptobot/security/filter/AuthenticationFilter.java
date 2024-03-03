package com.ai.cryptobot.security.filter;

import com.ai.cryptobot.common.Constant;
import com.ai.cryptobot.cryptobotUtils.ObjectsUtil;
import com.ai.cryptobot.security.MemberCustom;
import com.ai.cryptobot.security.dto.response.LoginResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public AuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(Constant.LOGIN_URL, "POST"));
        setUsernameParameter(USERNAME);
        setPasswordParameter(PASSWORD);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String username = request.getParameter(USERNAME);
            String password = request.getParameter(PASSWORD);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        MemberCustom user = (MemberCustom) authentication.getPrincipal(); //lay thong tin ng dung dang nhap
        final List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse loginResponse = jwtTokenProvider.generateToken(authentication);
        setResponse(loginResponse, user, authorities);
        response.setHeader("Content-Type", Constant.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().println(ObjectsUtil.toJson(loginResponse));
    }

    private void setResponse(LoginResponse loginResponse, MemberCustom user, List<String> authorities) {
        loginResponse.setFullName(user.getFullName());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setActive(user.isActive());
        loginResponse.setRoles(authorities);
    }
}

