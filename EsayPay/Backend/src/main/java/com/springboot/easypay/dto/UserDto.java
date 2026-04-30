package com.springboot.easypay.dto;

public record UserDto(
        String username,
         String role,
        boolean isActive) {
}
