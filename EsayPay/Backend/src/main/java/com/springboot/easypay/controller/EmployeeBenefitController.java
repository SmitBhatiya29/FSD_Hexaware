package com.springboot.easypay.controller;



import com.springboot.easypay.dto.AllBenefitResDto;
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
@CrossOrigin(origins = "http://localhost:5173/")
public class EmployeeBenefitController {
    private final EmployeeBenefitService employeeBenefitService;
    //done by Payroll Pro /..
    @PutMapping("/assign")
    public ResponseEntity<?> assignBenefit(Principal principal, @RequestBody EmployeeBenefitReqDto employeeBenefitReqDto) {
        String loggedInEmail = principal.getName();
        employeeBenefitService.assignBenefit(employeeBenefitReqDto,loggedInEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    //for payroll processor
    @GetMapping("/get-all-benefitReq")
    public AllBenefitResDto getAllBenefitReq(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                 @RequestParam(value = "size", required = false, defaultValue = "5") int size
){
        return employeeBenefitService.getAllBenefitReq(page,size);
    }

    //  EmployeeBenefitController
    @DeleteMapping("/revoke/{id}")
    public ResponseEntity<String> revokeBenefit(@PathVariable Long id) {
        employeeBenefitService.revokeBenefit(id);
        return ResponseEntity.ok("Benefit revoked and deleted successfully");
    }


}
