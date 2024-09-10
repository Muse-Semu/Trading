package com.ayg.trading.service.implemetations;

import com.ayg.trading.config.JwtProvider;
import com.ayg.trading.domain.VERIFICATION_TYPE;
import com.ayg.trading.model.TwoFactorAuth;
import com.ayg.trading.model.TwoFactorOTP;
import com.ayg.trading.model.User;
import com.ayg.trading.repository.UserRepository;
import com.ayg.trading.response.AuthResponse;
import com.ayg.trading.service.CustomUserDetailsService;
import com.ayg.trading.service.interfaces.EmailService;
import com.ayg.trading.service.interfaces.TwoFactorOtpService;
import com.ayg.trading.service.interfaces.UserService;
import com.ayg.trading.utils.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;

    private final TwoFactorOtpService twoFactorOtpService;

    private EmailService emailService;


    @Autowired
    public UserServiceImplementation(UserRepository userRepository, CustomUserDetailsService customUserDetailsService, TwoFactorOtpService twoFactorOtpService, EmailService emailService) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.twoFactorOtpService = twoFactorOtpService;
        this.emailService = emailService;
    }



    @Override
    public ResponseEntity<Object> createUser(User user) {
        // Check if email already exists in the database
        User isEmailExists = userRepository.findByEmail(user.getEmail());

        if (isEmailExists != null) {
            // Create a custom error message for email conflict
            String errorMessage = "Email already exists";
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }

        // Create new user object and set details
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setFullName(user.getFullName());

        // Save the user to the database
        userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()

        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.createToken(authentication);

        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(jwt);
        authResponse.setMessage("Successfully created user");
        authResponse.setStatus(true);

        // Return the saved user with HTTP status 201 (Created)
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> login(User user) {
        

        Authentication authentication =authenticate(
                user.getEmail(),
                user.getPassword()

        );



        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.createToken(authentication);

        User authUser = userRepository.findByEmail(authentication.getPrincipal().toString());


        if (user.getTwoFactorAuth().isEnabled()) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setJwt(jwt);
            authResponse.setStatus(true);
            authResponse.setMessage("Two factor is enabled for user");
            authResponse.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOtp();
            TwoFactorOTP oldTwoFactorOtp = twoFactorOtpService.findByUser(authUser.getId());
            if (oldTwoFactorOtp != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }

            TwoFactorOTP newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser,otp,jwt);

            authResponse.setSession(newTwoFactorOtp.getId());

            try {
                emailService.sendOtpVerificationEmail(user.getEmail(), otp);
            } catch (MessagingException e) {
                log.error("Message not sent ");
                throw new MailSendException(e.getMessage());
            }
            return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
        }


        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(jwt);
        authResponse.setMessage("logged in successfully");
        authResponse.setStatus(true);

        // Return the saved user with HTTP status 201 (Created)
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    /**
     * @param email 
     * @return
     */
    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return user;
    }

    /**
     * @param userId 
     * @return
     */
    @Override
    public User findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
           throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

    /**
     * @param jwtToken 
     * @return
     */
    @Override
    public User findUserProfileByJwtToken(String jwtToken) {
        String email = JwtProvider.getEmailFromToken(jwtToken);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return user;
    }

    /**
     * @param user 
     * @param verificationType
     * @param sendTo
     * @return
     */
    @Override
    public User enableTwoFactorAuthentication(User user, VERIFICATION_TYPE verificationType, String sendTo) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType.name());
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }


    /**
     * @param user 
     * @param newPassword
     * @return
     */
    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UsernameNotFoundException(email);
        }
        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

}
