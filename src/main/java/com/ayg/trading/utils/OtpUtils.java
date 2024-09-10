package com.ayg.trading.utils;

import java.util.Random;

public class OtpUtils {

    private static final Random random = new Random();
    OtpUtils() {
    }

    public static String generateOtp() {
        int otpLength = 6;

        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();

    }
}
