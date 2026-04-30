package com.springboot.easypay.dto;

import java.util.List;

public record PageBenefitResDto(
        List<BenfitResDto> data,
        long totalRecords,
        int totalPages

) {
}
