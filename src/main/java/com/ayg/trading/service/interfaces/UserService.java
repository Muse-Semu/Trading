package com.ayg.trading.service.interfaces;

import com.ayg.trading.domain.VERIFICATION_TYPE;
import com.ayg.trading.model.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<Object> createUser(User user);
    public ResponseEntity<Object> login(User user) throws MessagingException;
    public User findUserByEmail(String email);
    public User findUserById(Long userId);
    public User findUserProfileByJwtToken(String jwtToken);
    public User enableTwoFactorAuthentication(User user, VERIFICATION_TYPE verificationType, String sendTo);
    public User updatePassword(User user,String newPassword);

}
