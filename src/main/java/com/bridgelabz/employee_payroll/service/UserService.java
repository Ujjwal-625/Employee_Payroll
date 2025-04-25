package com.bridgelabz.employee_payroll.service;


import com.bridgelabz.employee_payroll.dto.*;
import com.bridgelabz.employee_payroll.exception.EmployeePayrollException;
import com.bridgelabz.employee_payroll.model.User;
import com.bridgelabz.employee_payroll.repository.UserRepository;
import com.bridgelabz.employee_payroll.utility.JWTUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Random;

@Slf4j // Lombok Logging
@Service
public class UserService implements UserInterface {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JWTUtility jwtUtility;

    @Autowired
    EmailService emailService;

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
        emailService.sendEmail(user.getEmail(), "Registered in Employee Payroll App", "Hi....\n You have been successfully registered!");

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
                emailService.sendEmail(user.getEmail(), "Logged in Employee Payroll App", "Hi....\n You have been successfully logged in! " + token);
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

    @Override
    public ResponseEntity<ResponseDTO> resetPassword(ResetPassdto resetPassdto) {
        User user =userRepository.findByEmail(resetPassdto.getEmail()).get();
        if(user==null) {
            throw new EmployeePayrollException("Email do not exist ");
        }
        if(user.getOtp().equals(resetPassdto.getOtp())){
            String pass=passwordEncoder.encode(resetPassdto.getPassword());
            user.setPassword(pass);
            user.setOtp(null);
            userRepository.save(user);
            ResponseDTO responseDTO=new ResponseDTO("User Password updated succesfully",user);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }
        ResponseDTO responseDTO=new ResponseDTO("User Not found",user);
        return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ResponseDTO> forgetPassword(ForgetPassdto forgetPassdto) {
        User user =userRepository.findByEmail(forgetPassdto.getEmail()).get();
        if(user==null) {
            throw new EmployeePayrollException("Email do not exist ");
        }
        String otp=String.valueOf(100000+new Random(900000).nextInt());
        emailService.sendEmail(forgetPassdto.getEmail(),"Reset Your Password","Otp to reset your password "+otp);

        user.setOtp(otp);
        userRepository.save(user);

        ResponseDTO responseDTO=new ResponseDTO("otp genrated succesfully",otp);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
}