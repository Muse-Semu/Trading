package com.ayg.trading.repository;

import com.ayg.trading.model.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
    TwoFactorOTP findByUserId(Long userId) ;
}
