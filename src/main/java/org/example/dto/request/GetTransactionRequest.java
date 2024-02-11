package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetTransactionRequest {
    @NotNull
    private String companyName;
    private String email;
}
