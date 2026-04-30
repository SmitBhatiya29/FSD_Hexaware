package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;

import java.math.BigDecimal;

public record EmployeeBenefitReqListDto(
        Long id,
        String benefitName,
        String description,
        BigDecimal benefitAmount,
        RequestStatus status
) {
}
