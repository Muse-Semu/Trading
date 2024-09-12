package com.ayg.trading.repository;

import com.ayg.trading.model.User;
import com.ayg.trading.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerififcationCodeRepository extends JpaRepository<VerificationCode,Long> {

    public User findByUserId(String userId);
}
