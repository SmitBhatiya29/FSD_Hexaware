package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;

import java.math.BigDecimal;

public record EmployeeBenefitReqDto(
        Long benefitId,
        BigDecimal benefitAmount,
        RequestStatus status
) {
}
