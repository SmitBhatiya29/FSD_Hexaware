package com.springboot.easypay.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpTimeSheetResDto(
        Long id,
        LocalDate workDate,
        BigDecimal hoursWorked,
        String project,
        String Description,
        String status
) {
}
