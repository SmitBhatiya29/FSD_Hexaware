package com.springboot.easypay.mapper;

import com.springboot.easypay.dto.EmployeeBenefitReqListDto;
import com.springboot.easypay.dto.PendingBenefitResDto;
import com.springboot.easypay.model.EmployeeBenefit;

public class EmployeeBenifitMapper {
    public static EmployeeBenefitReqListDto mapToDto(EmployeeBenefit employeeBenefit) {
        return new EmployeeBenefitReqListDto(
                employeeBenefit.getId(),
                employeeBenefit.getBenefit().getBenefitName(),
                employeeBenefit.getBenefit().getDescription(),
                employeeBenefit.getBenefitAmount(),
                employeeBenefit.getStatus()
        );
    }

    public static PendingBenefitResDto mapTogetAllResDto(EmployeeBenefit employeeBenefit) {
        return new PendingBenefitResDto(
                employeeBenefit.getId(),
                employeeBenefit.getEmployee().getFirstName(),
                employeeBenefit.getEmployee().getLastName(),
                employeeBenefit.getEmployee().getDepartment(),
                employeeBenefit.getBenefit().getBenefitName(),
                employeeBenefit.getBenefit().getDescription(),
                employeeBenefit.getBenefitAmount(),
                employeeBenefit.getStatus()
        );
    }
}
