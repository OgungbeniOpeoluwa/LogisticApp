package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T>{
    private T message;
    private boolean isSuccessfull;
}
