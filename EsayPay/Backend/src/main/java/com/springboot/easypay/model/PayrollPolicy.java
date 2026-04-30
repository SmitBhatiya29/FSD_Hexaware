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
@Table(name = "payroll_policies")
public class PayrollPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_salary", precision = 10, scale = 2, nullable = false)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 10, scale = 2, nullable = false)
    private BigDecimal maxSalary;

    @Column(name = "tax_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal taxPercentage;

    @Column(name = "hra_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal hraPercentage;

    @Column(name = "standard_deduction", precision = 10, scale = 2, nullable = false)
    private BigDecimal standardDeduction;
}
