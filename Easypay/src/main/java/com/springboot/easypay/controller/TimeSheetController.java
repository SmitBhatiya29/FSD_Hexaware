package com.springboot.easypay.controller;

import com.springboot.easypay.dto.TimeSheetReqDto;
import com.springboot.easypay.dto.TimeSheetResponseDto;
import com.springboot.easypay.dto.TimeSheetStatusReqDto;
import com.springboot.easypay.service.TimeSheetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/timesheets")
public class TimeSheetController {
    private final TimeSheetService timeSheetService;
    //any one can do    
    @PostMapping("/submit")
    public ResponseEntity<?> submitTimeSheet(@RequestBody TimeSheetReqDto dto, Principal principal) {
        String loggedInEmail = principal.getName();
        timeSheetService.submitTimeSheet(dto, loggedInEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

// access by manager only
    @GetMapping("/pending")
    public List<TimeSheetResponseDto> getPendingTimeSheets(Principal principal) {
        String loggedInEmail = principal.getName();
        return  timeSheetService.getPendingTimeSheetsForManager(loggedInEmail);

    }

    // access by manager only
    @PutMapping("/status/{timesheetId}")
    public ResponseEntity<?> updateTimeSheetStatus(
            @PathVariable Long timesheetId,
           Principal principal,
            @RequestBody TimeSheetStatusReqDto dto) {
        String loggedInEmail = principal.getName();
        timeSheetService.updateTimeSheetStatus(timesheetId,loggedInEmail,dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

