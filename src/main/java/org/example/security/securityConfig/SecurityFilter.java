package org.example.security.securityConfig;

import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.security.filter.UserAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.example.data.model.Roles.*;
import static org.springframework.http.HttpMethod.GET;

@Slf4j
@AllArgsConstructor
@Configuration

public class SecurityFilter {
    private AuthenticationManager authenticationManager;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        Filter filter = new UserAuthentication(authenticationManager);
        log.info("value()->{}", filter);
       return httpSecurity.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAt(filter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(c->c.requestMatchers("/login").permitAll())
                .authorizeHttpRequests((c)->c.requestMatchers(GET,"/api/v1/customer")
                       .hasAnyAuthority(CUSTOMER.name(),ADMINISTRATOR.name() ))
                .build();

    }
}
