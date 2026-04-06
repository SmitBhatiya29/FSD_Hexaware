package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.TimeSheetResponseDto;
import com.springboot.easypay.model.TimeSheet;

public class TimeSheetMapper {
    public static TimeSheetResponseDto mapToResponseDto(TimeSheet timeSheet) {
        return new TimeSheetResponseDto(
                timeSheet.getId(),
                timeSheet.getEmployee().getFirstName(),
                timeSheet.getEmployee().getLastName(),
                timeSheet.getWorkDate(),
                timeSheet.getHoursWorked(),
                timeSheet.getStatus(),
                timeSheet.getEmployee().getUser().getRole().toString()
        );
    }
}
