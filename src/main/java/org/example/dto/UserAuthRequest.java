package org.example.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserAuthRequest {
    private String username;
    private String password;
}
