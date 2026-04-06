package com.springboot.easypay.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TimeSheetReqDto(
        LocalDate workDate,
        BigDecimal hoursWorked
) {
}
