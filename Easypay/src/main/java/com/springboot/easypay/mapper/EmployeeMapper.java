package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.PendingEmployeeProfileResDto;
import com.springboot.easypay.model.Employee;

public class EmployeeMapper {
    public static PendingEmployeeProfileResDto mapToPendingProfileDto(Employee emp) {
        return new PendingEmployeeProfileResDto(
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getUser().getUserEmail(),
                emp.getUser().getRole().name(),
                emp.getDepartment(),
                emp.getDesignation()
        );
    }
}
