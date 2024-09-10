package com.ayg.trading.service.interfaces;

import com.ayg.trading.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<Object> createUser(User user);
    public ResponseEntity<Object> login(User user);
}
