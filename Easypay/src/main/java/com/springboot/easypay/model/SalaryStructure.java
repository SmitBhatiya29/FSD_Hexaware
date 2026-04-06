package com.springboot.easypay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SalaryStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "basic_salary", precision = 10, scale = 2, nullable = false)
    private BigDecimal basicSalary;

    @Column(name = "hra_allowance", precision = 10, scale = 2)
    private BigDecimal hraAllowance;

    @Column(name = "standard_deduction", precision = 10, scale = 2)
    private BigDecimal standardDeduction;

    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;


}
