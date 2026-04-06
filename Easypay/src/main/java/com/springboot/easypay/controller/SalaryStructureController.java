package com.springboot.easypay.controller;

import com.springboot.easypay.dto.SalaryStructureReqDto;
import com.springboot.easypay.dto.SalaryStructureResDto;
import com.springboot.easypay.dto.SalaryStructureUpdateReqDto;
import com.springboot.easypay.service.SalaryStructureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/salary-structure")
public class SalaryStructureController {
    private final SalaryStructureService salaryStructureService;
    // Authority : hr
    @PostMapping("/add")
    private ResponseEntity<?> addSalaryStructure(@Valid @RequestBody SalaryStructureReqDto salaryStructureReqDto){
        salaryStructureService.addSalaryStructure(salaryStructureReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    // Authority : hr
    @PutMapping("/update/{employeeId}")
    private ResponseEntity<?> updateSalaryStucture(@RequestBody SalaryStructureUpdateReqDto salaryStructureUpdateReqDto, @PathVariable Long employeeId){
        salaryStructureService.updateSalaryStucture(salaryStructureUpdateReqDto,employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //employee can get salary structure
    @GetMapping("/get/{employeeId}")
    public SalaryStructureResDto getSalaryStructure(@PathVariable("employeeId") long employeeId) {
        return salaryStructureService.getSalaryStructure(employeeId);
    }

}
