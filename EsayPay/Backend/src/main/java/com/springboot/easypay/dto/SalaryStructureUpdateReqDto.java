package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record SalaryStructureUpdateReqDto(
        BigDecimal basicSalary,
        BigDecimal hraAllowance,
        BigDecimal standardDeduction,
        BigDecimal taxPercentage
) {
}
