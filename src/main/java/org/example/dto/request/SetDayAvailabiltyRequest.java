package org.example.dto.request;

import lombok.Data;

@Data
public class SetDayAvailabiltyRequest {
    private String companyName;
    private String vechicleType;
    private int number;
}
