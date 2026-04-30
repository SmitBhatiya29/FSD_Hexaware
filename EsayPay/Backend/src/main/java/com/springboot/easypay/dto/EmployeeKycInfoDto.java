package com.springboot.easypay.dto;

public record EmployeeKycInfoDto(
        String aadharNo,
        String panNo,
        String bankAccountNo,
        String ifscCode,
        String currentAddress
) {
}
