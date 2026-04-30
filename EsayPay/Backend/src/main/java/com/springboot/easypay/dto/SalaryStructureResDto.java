package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record SalaryStructureResDto(
        BigDecimal basicSalary,
        BigDecimal hraAllowance,
        BigDecimal standardDeduction,
        BigDecimal taxPercentage
) {
}
