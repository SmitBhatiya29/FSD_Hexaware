package com.springboot.easypay.dto;

import java.util.List;

public record AllEmployessPageResDto(
        List<AllEmployessResDto> data,
        long totalRecords,
        int totalPages
) {
}
