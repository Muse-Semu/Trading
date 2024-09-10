package com.ayg.trading.service.implemetations;

import com.ayg.trading.config.JwtProvider;
import com.ayg.trading.model.User;
import com.ayg.trading.repository.UserRepository;
import com.ayg.trading.response.AuthResponse;
import com.ayg.trading.service.CustomUserDetailsService;
import com.ayg.trading.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
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

        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(jwt);
        authResponse.setMessage("logged in successfully");
        authResponse.setStatus(true);

        // Return the saved user with HTTP status 201 (Created)
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
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
