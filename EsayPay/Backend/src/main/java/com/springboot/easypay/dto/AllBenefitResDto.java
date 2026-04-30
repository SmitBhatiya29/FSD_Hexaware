package com.springboot.easypay.dto;

import java.util.List;

public record AllBenefitResDto(
        List<PendingBenefitResDto> data,
        long totalRecords,
        int totalPages
) {
}
