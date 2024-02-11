package org.example.dto.request;

import lombok.Data;

@Data
public class DeleteVechicleRequest {
    private String companyName;
    private String vechicleType;
}
