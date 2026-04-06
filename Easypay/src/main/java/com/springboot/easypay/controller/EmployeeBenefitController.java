package com.springboot.easypay.controller;


import com.springboot.easypay.dto.EmployeeBenefitReqDto;
import com.springboot.easypay.service.EmployeeBenefitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employee-benefits")
public class EmployeeBenefitController {
    private final EmployeeBenefitService employeeBenefitService;
    @PostMapping("/assign")
    public ResponseEntity<?> assignBenefit(Principal principal, @RequestBody EmployeeBenefitReqDto employeeBenefitReqDto) {
        String loggedInEmail = principal.getName();
        employeeBenefitService.assignBenefit(employeeBenefitReqDto,loggedInEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
