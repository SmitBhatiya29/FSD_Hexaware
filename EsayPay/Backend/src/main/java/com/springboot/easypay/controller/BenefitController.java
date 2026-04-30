package com.springboot.easypay.controller;

import com.springboot.easypay.dto.BenefitReqDto;
import com.springboot.easypay.dto.PageBenefitResDto;
import com.springboot.easypay.service.BenefitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@AllArgsConstructor
@RequestMapping("/api/benefits")
@CrossOrigin(origins = "http://localhost:5173/")
public class BenefitController {
    private final BenefitService benefitService;
    @PostMapping("/add")
    public ResponseEntity<?> addBenefit(@RequestBody BenefitReqDto benefitReqDto) {
        benefitService.addBenefit(benefitReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public PageBenefitResDto getBenefit(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ){
        return benefitService.getBenefit(page,size);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBenefit(@PathVariable Long id,@RequestBody BenefitReqDto benefitReqDto){
        benefitService.updateBenefit(id,benefitReqDto);
        return ResponseEntity.ok().body("Benefit updated Successfully...!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBenefit(@PathVariable Long id){
        benefitService.deleteBenefit(id);
        return ResponseEntity.ok().body("Benefit deleted successfully......!");
    }
}
