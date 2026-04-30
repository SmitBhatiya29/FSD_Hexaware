package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.EmpTimeSheetResDto;
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
                timeSheet.getProject(),
                timeSheet.getDescription(),
                timeSheet.getStatus(),
                timeSheet.getEmployee().getUser().getRole().toString()
        );
    }

    public static EmpTimeSheetResDto mapToEmpResDto(TimeSheet timeSheet) {
        return new EmpTimeSheetResDto(
                timeSheet.getId(),
                timeSheet.getWorkDate(),
                timeSheet.getHoursWorked(),
                timeSheet.getProject(),
                timeSheet.getDescription(),
                timeSheet.getStatus().toString()
        );
    }
}
