package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TimeSheetResponseDto(
        Long timesheetId,
        String firstName,
        String lastName,
        LocalDate workDate,
        BigDecimal hoursWorked,
        RequestStatus status,
        String role
) {
}
