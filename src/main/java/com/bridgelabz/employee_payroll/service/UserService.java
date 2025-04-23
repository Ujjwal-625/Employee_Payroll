package com.bridgelabz.employee_payroll.service;


import com.bridgelabz.employee_payroll.dto.LoginDTO;
import com.bridgelabz.employee_payroll.dto.RegisterDTO;
import com.bridgelabz.employee_payroll.dto.ResponseDTO;
import com.bridgelabz.employee_payroll.model.User;
import com.bridgelabz.employee_payroll.repository.UserRepository;
import com.bridgelabz.employee_payroll.utility.JWTUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j // Lombok Logging
@Service
public class UserService implements UserInterface {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JWTUtility jwtUtility;


    @Override
    public ResponseDTO registerUser(RegisterDTO registerDTO) {
        log.info("Registering user: {}", registerDTO.getEmail());
        ResponseDTO res = new ResponseDTO("User already exist",null);
        if (existsByEmail(registerDTO.getEmail())) {
            log.warn("Registration failed: User already exists with email {}", registerDTO.getEmail());
            res.setMessage("error");
            res.setData("User Already Exists");
            return res;
        }

        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        log.info("User {} registered successfully", user.getEmail());
        // emailService.sendEmail(user.getEmail(), "Registered in Employee Payroll App", "Hi....\n You have been successfully registered!");

        res.setMessage("message");
        res.setData("User Registered Successfully");
        return res;
    }

    @Override
    public ResponseDTO loginUser(LoginDTO loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getEmail());
        ResponseDTO res = new ResponseDTO();
        Optional<User> userExists = getUserByEmail(loginDTO.getEmail());


        if (userExists.isPresent()) {
            User user = userExists.get();
            if (matchPassword(loginDTO.getPassword(), user.getPassword())) {
                String token = jwtUtility.generateToken(user.getEmail());
                user.setToken(token);
                userRepository.save(user);

                log.debug("Login successful for user: {} - Token generated", user.getEmail());
                // emailService.sendEmail(user.getEmail(), "Logged in Employee Payroll App", "Hi....\n You have been successfully logged in! " + token);
                res.setMessage("message");
                res.setData("User Logged In Successfully: " + token);
                return res;
            } else {
                log.warn("Invalid credentials for user: {}", loginDTO.getEmail());
                res.setMessage("error");
                res.setData("Invalid Credentials");
                return res;
            }
        }

        log.error("User not found with email: {}", loginDTO.getEmail());
        res.setMessage("error");
        res.setData("User Not Found");
        return res;
    }

    @Override
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        log.debug("Matching password for login attempt");
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists by email: {}", email);
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }
}