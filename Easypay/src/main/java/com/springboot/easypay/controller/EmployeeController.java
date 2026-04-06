package com.springboot.easypay.controller;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/signup")
    private ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto){
        employeeService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/submit-profile")
    public ResponseEntity<?> submitProfile(@RequestBody EmployeeProfileSubmitDto dto, Principal principal) {
        String loggedInEmail = principal.getName();
        employeeService.submitEmployeeDetails(dto,loggedInEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add-kyc-info")
    public ResponseEntity<?> addKycInfo(Principal principal,
                                        @RequestBody EmployeeKycInfoDto dto) {
        String loggedInEmail = principal.getName();
        employeeService.addKycInfo(loggedInEmail,dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/leave-request")
    public List<LeaveReqOfEmpResDto> getLeaveRequest(Principal principal) {
        String loggedInEmail = principal.getName();

        return employeeService.getLeaveRequest(loggedInEmail);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updateEmployeePassword(@RequestBody EmployeePasswordUpdateDto dto){
        employeeService.updateEmployeePassword(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
