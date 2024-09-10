package com.ayg.trading.service.implemetations;

import com.ayg.trading.model.TwoFactorOTP;
import com.ayg.trading.model.User;
import com.ayg.trading.repository.TwoFactorOtpRepository;
import com.ayg.trading.service.interfaces.TwoFactorOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImplemetation implements TwoFactorOtpService {

    private TwoFactorOtpRepository twoFactorOtpRepository;

    @Autowired
    public TwoFactorOtpServiceImplemetation(TwoFactorOtpRepository otpRepository) {
        this.twoFactorOtpRepository = otpRepository;
    }

    /**
     * @param user 
     * @param otp
     * @param jwt
     * @return
     */
    @Override
    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        TwoFactorOTP twoFactorOtp = new TwoFactorOTP();
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
        twoFactorOtp.setId(id);
        twoFactorOtp.setUser(user);
        return twoFactorOtpRepository.save(twoFactorOtp);
    }

    /**
     * @param twoFactorOTP 
     * @param otp
     * @return
     */
    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    /**
     * @param twoFactorOTP 
     */
    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {
        twoFactorOtpRepository.delete(twoFactorOTP);
    }

    /**
     * @param userId 
     * @return
     */
    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> otp = twoFactorOtpRepository.findById(id);
        return otp.orElse(null);
    }
}
