package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class FundWalletRequest {
    private int amount;
}
