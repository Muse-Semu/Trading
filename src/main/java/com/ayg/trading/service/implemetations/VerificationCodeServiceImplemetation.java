package com.ayg.trading.service.implemetations;

import com.ayg.trading.domain.VERIFICATION_TYPE;
import com.ayg.trading.model.User;
import com.ayg.trading.model.VerificationCode;
import com.ayg.trading.repository.VerififcationCodeRepository;
import com.ayg.trading.service.interfaces.VerificationCodeService;
import com.ayg.trading.utils.OtpUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImplemetation implements VerificationCodeService {

    private final VerififcationCodeRepository verififcationCodeRepository;

    public VerificationCodeServiceImplemetation(VerififcationCodeRepository verififcationCodeRepository) {
        this.verififcationCodeRepository = verififcationCodeRepository;
    }

    /**
     * @param user 
     * @param verificationType
     * @return
     */
    @Override
    public VerificationCode sendVerificationCode(User user, VERIFICATION_TYPE verificationType) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setOtp(OtpUtils.generateOtp());
        verificationCode.setType(verificationType);

        return verififcationCodeRepository.save(verificationCode);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public VerificationCode getVerificationCodeById(Long id) {
        Optional<VerificationCode> verificationCode = verififcationCodeRepository.findById(id);
        if (verificationCode.isPresent()) {
            return verificationCode.get();
        }
        throw new RuntimeException("Verification code not found");
    }

    /**
     * @param userId 
     * @return
     */
    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {

        return verififcationCodeRepository.findByUserId(userId);
    }

    /**
     * @param id 
     */
    @Override
    public void deleteVerificationCodeById(Long id) {

    }
}
