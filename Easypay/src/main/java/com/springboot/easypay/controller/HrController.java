package com.springboot.easypay.controller;

import com.springboot.easypay.dto.AllManagerDetailsResDto;
import com.springboot.easypay.dto.HRApprovalReqDto;
import com.springboot.easypay.dto.PendingEmployeeProfilePagResDto;
import com.springboot.easypay.enums.Role;
import com.springboot.easypay.service.HrService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/hr")
public class HrController {
    private final HrService hrService;

    //Hr can get Manager list to assign to new Employee
    @GetMapping("/all-manager")
    public List<AllManagerDetailsResDto> getAllManager(){
        return hrService.getAllManager();
    }

    @GetMapping("/pending-profiles")
    public PendingEmployeeProfilePagResDto getPendingProfiles(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "role", required = false) Role role,
            @RequestParam(value = "department", required = false) String department) {
        return hrService.getAllPendingProfiles(page, size,role,department);
    }

    @PutMapping("/approve-employee/{employeeId}")
    public ResponseEntity<?> approveEmployee(@PathVariable Long employeeId,
                                             @RequestBody HRApprovalReqDto dto) {
        hrService.approveEmployeeByHR(employeeId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Employee approved successfully!");
    }


}
