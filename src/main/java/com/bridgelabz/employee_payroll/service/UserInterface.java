package com.bridgelabz.employee_payroll.service;

import com.bridgelabz.employee_payroll.dto.LoginDTO;
import com.bridgelabz.employee_payroll.dto.RegisterDTO;
import com.bridgelabz.employee_payroll.dto.ResponseDTO;
import com.bridgelabz.employee_payroll.model.User;

import java.util.Optional;

public interface UserInterface {

    ResponseDTO registerUser(RegisterDTO registerDTO);

    ResponseDTO loginUser(LoginDTO loginDTO);

    boolean matchPassword(String rawPassword, String encodedPassword);

    boolean existsByEmail(String email);

    Optional<User> getUserByEmail(String email);
}
