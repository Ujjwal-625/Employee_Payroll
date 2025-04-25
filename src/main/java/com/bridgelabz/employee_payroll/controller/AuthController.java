package com.bridgelabz.employee_payroll.controller;


import com.bridgelabz.employee_payroll.dto.*;
import com.bridgelabz.employee_payroll.service.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInterface userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterDTO registerDTO) {
        ResponseDTO response = userService.registerUser(registerDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO loginDTO) {
        ResponseDTO response = userService.loginUser(loginDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPassdto resetPassdto){
        return userService.resetPassword(resetPassdto);
    }

    @PostMapping("/forget")
    public ResponseEntity<ResponseDTO> forgetPassword(@RequestBody ForgetPassdto forgetPassdto){
        return userService.forgetPassword(forgetPassdto);
    }
}
