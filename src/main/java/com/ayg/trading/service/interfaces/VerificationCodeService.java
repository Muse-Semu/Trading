package com.ayg.trading.service.interfaces;


import com.ayg.trading.domain.VERIFICATION_TYPE;
import com.ayg.trading.model.User;
import com.ayg.trading.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VERIFICATION_TYPE verificationType);
    VerificationCode getVerificationCodeById(Long id);
    VerificationCode getVerificationCodeByUser(Long userId);
    void deleteVerificationCodeById(Long id);
}
