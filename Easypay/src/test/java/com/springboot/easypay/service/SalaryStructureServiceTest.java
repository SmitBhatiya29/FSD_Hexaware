package com.springboot.easypay.service;

import com.springboot.easypay.controller.SalaryStructureController;
import com.springboot.easypay.dto.SalaryStructureResDto;
import com.springboot.easypay.model.SalaryStructure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SalaryStructureServiceTest {
    @Mock
    private SalaryStructureService salaryStructureService;
    @InjectMocks
    private SalaryStructureController salaryStructureController;
    @Test
public void getSalaryStructure() {
    long employeeId = 1L;
    SalaryStructureResDto dto = new SalaryStructureResDto(
            new BigDecimal("50000.00"),
            new BigDecimal("10000.00"),
            new BigDecimal("2000.00"),
            new BigDecimal("10.0")
    );

    Mockito.when(salaryStructureService.getSalaryStructure(employeeId)).thenReturn(dto);

    SalaryStructureResDto result = salaryStructureController.getSalaryStructure(employeeId);

    Assertions.assertEquals(new BigDecimal("50000.00"),result.basicSalary());
    Assertions.assertEquals(new BigDecimal("10.0"),result.taxPercentage());

    Mockito.verify(salaryStructureService, times(1)).getSalaryStructure(employeeId);
}

}
