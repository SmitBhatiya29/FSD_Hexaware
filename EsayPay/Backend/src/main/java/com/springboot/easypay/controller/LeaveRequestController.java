package com.springboot.easypay.controller;

import com.springboot.easypay.dto.LeaveReqDto;
import com.springboot.easypay.dto.LeaveTypeDto;
import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.service.LeaveRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/leave-request")
@CrossOrigin(origins = "http://localhost:5173/")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    //employee and add leave req.....
    @PostMapping("/add")
    public ResponseEntity<?> addLeaveRequest(Principal principal, @RequestBody LeaveReqDto dto) {
        String loggedUserEmail = principal.getName();
        leaveRequestService.addLeaveRequest(loggedUserEmail,dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{LeaveRequestId}")
    public ResponseEntity<?> deleteLeaveRequest(Principal principal,@PathVariable Long LeaveRequestId) {
        String loggedUserEmail = principal.getName();
        leaveRequestService.deleteLeaveRequest(loggedUserEmail,LeaveRequestId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/type")
    public List<LeaveTypeDto> getLeaveType(){
        List<LeaveTypeDto> responseList = new ArrayList<>();
        for (LeaveType type : LeaveType.values()){
            responseList.add(new LeaveTypeDto(type.name()) );
        }
        return responseList;
    }

}
