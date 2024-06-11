package org.example.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String phoneNumber;
    private String email;
}
