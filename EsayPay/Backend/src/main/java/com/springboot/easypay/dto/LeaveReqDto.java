package com.springboot.easypay.dto;

import com.springboot.easypay.enums.LeaveType;

import java.time.LocalDate;

public record LeaveReqDto(
        LeaveType leaveType,
        LocalDate fromDate,
        LocalDate toDate,
        String details
) {
}
