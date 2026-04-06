package com.springboot.easypay.dto;

import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.enums.RequestStatus;

import java.time.LocalDate;

public record LeaveReqListOfManagerResDto(
        Long id,
        String employeeName,
        LeaveType leaveType,
        RequestStatus status,
        LocalDate fromDate,
        LocalDate toDate,
        int numofDays,
        String details
) {
}
