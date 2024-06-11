package org.example.security;

import lombok.AllArgsConstructor;
import org.example.data.model.Customers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@AllArgsConstructor
public class UserConfig implements UserDetails {
    private Customers customer;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customer.getUserRole().stream()
                .map((x)->new SimpleGrantedAuthority(x.name())).toList();
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
