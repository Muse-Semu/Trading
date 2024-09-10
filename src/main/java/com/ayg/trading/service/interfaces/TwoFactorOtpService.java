package com.ayg.trading.service.interfaces;

import com.ayg.trading.model.TwoFactorOTP;
import com.ayg.trading.model.User;

public interface TwoFactorOtpService {
    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);
    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);
}
