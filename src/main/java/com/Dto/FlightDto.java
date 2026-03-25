package com.Dto;

import com.model.Employee;

import java.util.List;

public record FlightDto(
        String flightNumber,
        String source,
        String destination,
        String airlineName,
        List<Employee> employees
) {
}
