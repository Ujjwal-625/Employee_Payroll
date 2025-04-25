package com.bridgelabz.employee_payroll.service;

import com.bridgelabz.employee_payroll.dto.*;
import com.bridgelabz.employee_payroll.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserInterface {

    ResponseDTO registerUser(RegisterDTO registerDTO);

    ResponseDTO loginUser(LoginDTO loginDTO);

    boolean matchPassword(String rawPassword, String encodedPassword);

    boolean existsByEmail(String email);

    Optional<User> getUserByEmail(String email);


    ResponseEntity<ResponseDTO> resetPassword(ResetPassdto resetPassdto);

    ResponseEntity<ResponseDTO> forgetPassword(ForgetPassdto forgetPassdto);
}
