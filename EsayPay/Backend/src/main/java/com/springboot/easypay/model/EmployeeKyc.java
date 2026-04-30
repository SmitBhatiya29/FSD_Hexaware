package com.springboot.easypay.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "employee_kyc")
public class EmployeeKyc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "aadhar_no", unique = true, length = 12)
    private String aadharNo;

    @Column(name = "pan_no", unique = true, length = 10)
    private String panNo;

    @Column(name = "bank_account_no", length = 20)
    private String bankAccountNo;

    @Column(name = "ifsc_code", length = 15)
    private String ifscCode;

    @Column(name = "current_address", length = 255)
    private String currentAddress;

}


