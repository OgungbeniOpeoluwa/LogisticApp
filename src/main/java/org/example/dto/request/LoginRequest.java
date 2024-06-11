package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginRequest {
    @NotNull(message = "email should not be null")
    private String username;
    @NotNull(message ="Password should not be null")
    private String password;
}
