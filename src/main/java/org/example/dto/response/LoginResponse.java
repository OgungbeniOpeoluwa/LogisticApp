package org.example.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String refresh;
    private String access;
}
