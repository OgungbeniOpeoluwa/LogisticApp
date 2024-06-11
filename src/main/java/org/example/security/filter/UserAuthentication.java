package org.example.security.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserAuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;


public class UserAuthentication extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    public UserAuthentication(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = null;
        UserAuthRequest requests = null;
        try {
            inputStream = request.getInputStream();
            requests = mapper.readValue(inputStream, UserAuthRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(requests.getUsername(),requests.getPassword());
        authenticationManager.authenticate(authentication);
        if(authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
            return authentication;
//        }throw  new BadCredentialsException("Invalid details");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        String authResponse = JWT.create()
                    .withExpiresAt(Instant.now().plusSeconds(8600 * 7))
                    .withClaim(authResult.getPrincipal().toString(),new Date())
                  .sign(Algorithm.HMAC256("This is a secrete key"));
        HashMap<String,String> authToken = new HashMap<>();
        authToken.put("access-token",authResponse);
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(authToken));
        response.flushBuffer();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        super.unsuccessfulAuthentication(request, response, failed);
    }
}
