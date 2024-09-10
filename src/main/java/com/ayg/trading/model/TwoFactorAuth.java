package com.ayg.trading.model;

import com.ayg.trading.domain.VERIFICATION_TYPE;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnabled = false;
    private VERIFICATION_TYPE verificationType;
    private String sendTo;

}
