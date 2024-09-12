package com.ayg.trading.controller;

import com.ayg.trading.config.JwtConstant;
import com.ayg.trading.domain.VERIFICATION_TYPE;
import com.ayg.trading.model.TwoFactorOTP;
import com.ayg.trading.model.User;
import com.ayg.trading.response.AuthResponse;
import com.ayg.trading.service.implemetations.TwoFactorOtpServiceImplemetation;
import com.ayg.trading.service.interfaces.TwoFactorOtpService;
import com.ayg.trading.service.interfaces.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {


    private final UserService userService;
    private final TwoFactorOtpService twoFactorOtpService;

    @Autowired
    public UserController(UserService userService, TwoFactorOtpServiceImplemetation twoFactorOtpServiceImplemetation, TwoFactorOtpService twoFactorOtpService) {
        this.userService = userService;
        this.twoFactorOtpService = twoFactorOtpService;
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
       return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) throws MessagingException {
        return userService.login(user);
    }

    @PostMapping("/two-factor/")
    public ResponseEntity<AuthResponse> verifySigningOtp(@PathVariable String otp, @RequestParam String id) throws MessagingException {
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP,otp)){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("2FA verified");
            authResponse.setStatus(true);
            authResponse.setTwoFactorAuthEnabled(true);
            authResponse.setJwt(twoFactorOTP.getJwt());

          return ResponseEntity.ok(authResponse);
        }

        throw new MessagingException("2FA verification failed");

    }

    @GetMapping("/profile/otp/{otp}")
    public ResponseEntity<User> getUserProfile(@RequestHeader(JwtConstant.HEADER_STRING) String jwt)  {
        User user = userService.findUserProfileByJwtToken(jwt) ;
        return ResponseEntity.ok(user);
    }


    @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<User> sendVerificationOtpCode(
            @RequestHeader(JwtConstant.HEADER_STRING) String jwt,
            @PathVariable VERIFICATION_TYPE verificationType)  {
        User user = userService.findUserProfileByJwtToken(jwt) ;
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> getTwoFactorAuthentication(@RequestHeader(JwtConstant.HEADER_STRING) String jwt)  {
        User user = userService.findUserProfileByJwtToken(jwt) ;
        return ResponseEntity.ok(user);
    }


}
