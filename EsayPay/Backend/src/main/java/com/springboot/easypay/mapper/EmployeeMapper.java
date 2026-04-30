package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.AllEmployessResDto;
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

    public static AllEmployessResDto mapToAllEmployeeProfileDto(Employee emp) {
        return new AllEmployessResDto(
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getUser().getUserEmail(),
                emp.getDesignation(),
                emp.getDepartment(),
                emp.getJoiningDate().toString(),
                emp.getUser().getRole(),
                emp.getStatus()

        );
    }
}
