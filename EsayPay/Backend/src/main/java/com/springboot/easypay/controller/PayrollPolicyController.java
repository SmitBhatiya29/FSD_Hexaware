package com.springboot.easypay.controller;

import com.springboot.easypay.dto.PayrollPolicyReqDto;
import com.springboot.easypay.model.PayrollPolicy;
import com.springboot.easypay.service.PayrollPolicyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/payroll-policies")
@AllArgsConstructor
public class PayrollPolicyController {

    private final PayrollPolicyService payrollPolicyService;

    @PostMapping("/add")
    public ResponseEntity<String> addPolicy(@RequestBody PayrollPolicyReqDto dto) {
        payrollPolicyService.addPayrollPolicy(dto);
        return new ResponseEntity<>("Payroll Policy added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PayrollPolicy>> getAllPolicies() {
        return new ResponseEntity<>(payrollPolicyService.getAllPolicies(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")

    public ResponseEntity<String> updatePolicy(@PathVariable Long id, @RequestBody PayrollPolicyReqDto dto) {
        payrollPolicyService.updatePayrollPolicy(id, dto);
        return new ResponseEntity<>("Payroll Policy updated successfully", HttpStatus.OK);
    }

}
