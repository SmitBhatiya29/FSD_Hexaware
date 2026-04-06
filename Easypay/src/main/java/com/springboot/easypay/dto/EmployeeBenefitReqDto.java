package com.springboot.easypay.dto;

import java.math.BigDecimal;

public record EmployeeBenefitReqDto(
        Long benefitId,
        BigDecimal benefitAmount

) {
}
