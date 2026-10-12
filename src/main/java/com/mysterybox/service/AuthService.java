package com.mysterybox.service;

import com.mysterybox.dto.AuthResponse;
import com.mysterybox.dto.LoginRequest;
import com.mysterybox.dto.RegisterRequest;
import com.mysterybox.dto.UserDto;
import com.mysterybox.entity.Role;
import com.mysterybox.entity.User;
import com.mysterybox.exception.BadRequestException;
import com.mysterybox.repository.UserRepository;
import com.mysterybox.security.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("An account with this email address already exists.");
        }

        Role role = Role.USER;
        if (request.getRole() != null && "ADMIN".equalsIgnoreCase(request.getRole().trim())) {
            role = Role.ADMIN;
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword().trim());
        User user = new User(request.getName().trim(), email, encodedPassword, role);
        User savedUser = userRepository.save(user);

        String token = jwtUtils.generateToken(savedUser.getEmail(), savedUser.getRole().name());
        UserDto userDto = new UserDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());

        return new AuthResponse(token, userDto);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword().trim(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole());

        return new AuthResponse(token, userDto);
    }

    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("User profile not found"));
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
