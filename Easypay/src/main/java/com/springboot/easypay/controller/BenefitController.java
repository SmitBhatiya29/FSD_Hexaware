package com.springboot.easypay.controller;

import com.springboot.easypay.dto.BenefitReqDto;
import com.springboot.easypay.service.BenefitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/benefits")
public class BenefitController {
    private final BenefitService benefitService;
    @PostMapping("/add")
    public ResponseEntity<?> addBenefit(@RequestBody BenefitReqDto benefitReqDto) {
        benefitService.addBenefit(benefitReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
