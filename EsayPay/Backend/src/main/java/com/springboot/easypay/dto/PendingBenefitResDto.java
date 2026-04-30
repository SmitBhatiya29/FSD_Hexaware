package com.springboot.easypay.dto;

import com.springboot.easypay.enums.RequestStatus;

import java.math.BigDecimal;

public record PendingBenefitResDto(
        Long id,
        String firstName,
        String lastName,
        String department,
        String benefitName,
        String description,
        BigDecimal benefitAmount,
        RequestStatus status
) {
}
