package com.springboot.easypay.controller;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.service.EmployeeBenefitService;
import com.springboot.easypay.service.EmployeeService;
import com.springboot.easypay.service.TimeSheetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@AllArgsConstructor
@RequestMapping("/api/employee")


public class EmployeeController {

    private final EmployeeService employeeService;
    private final TimeSheetService timeSheetService;
    private final EmployeeBenefitService employeeBenefitService;


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



    @GetMapping("/profile-details")
    public  EmployeeProfileResDto getProfile(Principal principal){
        String loggedInEmail = principal.getName();
        return employeeService.getProfile(loggedInEmail);
    }




    //normal list : get leave req
    @GetMapping("/leave-request")
    public List<LeaveReqOfEmpResDto> getLeaveRequest(Principal principal,
    @RequestParam(required = false)RequestStatus status,
    @RequestParam(required = false) LeaveType leaveType) {
        String loggedInEmail = principal.getName();
        return employeeService.getLeaveRequest(loggedInEmail,status, leaveType);
    }

    //pagination v1 get leave req
    @GetMapping("/leave-request/v1")
    public PageLeaveReqOfEmpResDto getLeaveRequestPage(Principal principal,
                                                     @RequestParam(required = false)RequestStatus status,
                                                     @RequestParam(required = false) LeaveType leaveType,
                                                     @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        String loggedInEmail = principal.getName();
        return employeeService.getLeaveRequestPage(loggedInEmail,status, leaveType,page,size);
    }



    @GetMapping("/time-sheet")
    public List<EmpTimeSheetResDto> getTimeSheet(Principal principal,
                                                  @RequestParam(required = false)RequestStatus status) {
        String loggedInEmail = principal.getName();
        return timeSheetService.getTimeSheet(loggedInEmail,status);
    }

    @GetMapping("/time-sheet/v1")
    public PageEmpTimeSheetResDto getTimeSheetPage(Principal principal,
                                                 @RequestParam(required = false)RequestStatus status,
                                                   @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        String loggedInEmail = principal.getName();
        return timeSheetService.getTimeSheetPage(loggedInEmail,status,page,size);
    }



    @PostMapping("/benefit-request")
    public ResponseEntity<?> requestBenefit(Principal principal, @RequestParam Long benefitId) {
        try {
            String loggedInEmail = principal.getName();
            employeeBenefitService.requestBenefit(benefitId, loggedInEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body("Benefit request submitted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }



    //get benefit by employee id
    @GetMapping("/get/benefit-reqest/v2/{employeeId}")
    public BenefitReqPagResDto getBenefitRequestByEmployeeIdV2(
            @PathVariable Long employeeId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ){
        return employeeBenefitService.getBenefitRequestByEmployeeId(employeeId, page, size);
    }


    //get by principal
    @GetMapping("/get/benefit-reqest")
    public BenefitReqPagResDto getMyBenefitReqest(
            Principal principal,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ){
        String loggedInEmail = principal.getName();
        return employeeBenefitService.getMyBenefitReqest(loggedInEmail,page,size);
    }




    //done by pp
    @DeleteMapping("/delete-benefit-request/{requestId}")
    public ResponseEntity<?> deleteBenefitRequest(Principal principal, @PathVariable Long requestId) {
        try {
            String loggedInEmail = principal.getName();
            employeeBenefitService.deleteBenefitRequest(requestId, loggedInEmail);
            return ResponseEntity.ok("Benefit request deleted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updateEmployeePassword(@RequestBody EmployeePasswordUpdateDto dto){
        employeeService.updateEmployeePassword(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/stats-pending-benefit")
    public ResponseEntity<Integer> getPendingBenefitRequest(Principal principal){
        String loggedInEmail = principal.getName();
        int count = employeeBenefitService.getPendingBenefitReq(loggedInEmail);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/add-kyc-info")
    public ResponseEntity<?> addKycInfo(Principal principal,
                                        @RequestBody EmployeeKycInfoDto dto) {
        String loggedInEmail = principal.getName();
        employeeService.addKycInfo(loggedInEmail,dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
