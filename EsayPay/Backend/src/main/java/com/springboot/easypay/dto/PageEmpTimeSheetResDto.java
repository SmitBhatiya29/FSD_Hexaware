package com.springboot.easypay.dto;

import java.util.List;

public record PageEmpTimeSheetResDto(
        List<EmpTimeSheetResDto> data,
        long totalRecords,
        int totalPages

) {
}
