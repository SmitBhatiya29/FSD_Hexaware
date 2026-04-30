package com.springboot.easypay.dto;

import java.util.List;

public record BenefitReqPagResDto(
        List<EmployeeBenefitReqListDto> data,
        long totalRecords,
        int totalPages
) {
}
