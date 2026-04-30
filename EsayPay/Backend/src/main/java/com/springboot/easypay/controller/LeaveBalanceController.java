package com.springboot.easypay.controller;

import com.springboot.easypay.dto.LeaveBalanceStatResDto;
import com.springboot.easypay.service.LeaveBalanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("api/leave-balance")
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;
    @GetMapping("/stats")
    public List<LeaveBalanceStatResDto> getLeaveBalanceStat(Principal principal){
        String loggedInEmail = principal.getName();
        return leaveBalanceService.getLeaveBalanceStat(loggedInEmail);
    }
}
