package com.mitesh.security.RestSecurity.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        if(authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(authorizationHeader);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String authorizationHeader) {

        if(authorizationHeader != null) {
            String userNameFromJwt = JWT.require(HMAC512(SecurityConstants.SIGNING_SECRET.getBytes()))
                                    .build()
                                    .verify(authorizationHeader.replace(SecurityConstants.BEARER_TOKEN_PREFIX, ""))
                                    .getSubject();

            if(userNameFromJwt != null) {
                return new UsernamePasswordAuthenticationToken(userNameFromJwt, null, new ArrayList<>());
            }
        }

        return null;
    }
}