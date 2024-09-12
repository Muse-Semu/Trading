package com.ayg.trading.model;

import com.ayg.trading.domain.VERIFICATION_TYPE;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;
    private String otp;
    private String email;
    @OneToOne
    private User user;
    private String mobile;
    private VERIFICATION_TYPE type;

}
