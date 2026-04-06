package com.springboot.easypay.controller;

import com.springboot.easypay.dto.LeaveReqDto;
import com.springboot.easypay.service.LeaveRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/leave-request")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    //employee and add leave req.....
    @PostMapping("/add")
    public ResponseEntity<?> addLeaveRequest(Principal principal, @RequestBody LeaveReqDto dto) {
        String loggedUserEmail = principal.getName();
        leaveRequestService.addLeaveRequest(loggedUserEmail,dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
