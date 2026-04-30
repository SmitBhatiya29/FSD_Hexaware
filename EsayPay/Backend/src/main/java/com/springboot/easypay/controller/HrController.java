package com.springboot.easypay.controller;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.enums.Role;
import com.springboot.easypay.service.HrService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
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

    @GetMapping("/all-employee")
    public AllEmployessPageResDto allEmployee(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "role", required = false) Role role,
            @RequestParam(value = "department", required = false) String department
    ){
        return hrService.allEmployee(page, size,role,department);
    }

    @GetMapping("/department-overview")
    public ResponseEntity<HrTotalEmployeeAnddepStatsResDto> getDepartmentOverview() {
        HrTotalEmployeeAnddepStatsResDto response = hrService.getDepartmentOverview();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type")
    public List<RoleResDto> getRoles(){
        List<RoleResDto> responseList = new ArrayList<>();
        for (Role role : Role.values()){
            responseList.add(new RoleResDto(role.name()) );
        }
        return responseList;
    }
}
