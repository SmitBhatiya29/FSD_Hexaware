package com.springboot.easypay.dto;

public record AllManagerDetailsResDto(
        long managerId,
        String firstName,
        String lastName,
        String department
) {

}
