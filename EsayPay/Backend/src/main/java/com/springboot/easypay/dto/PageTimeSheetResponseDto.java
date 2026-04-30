package com.springboot.easypay.dto;

import java.util.List;

public record PageTimeSheetResponseDto(
        List<TimeSheetResponseDto> data,
        long totalRecords,
        int totalPages
) {
}
