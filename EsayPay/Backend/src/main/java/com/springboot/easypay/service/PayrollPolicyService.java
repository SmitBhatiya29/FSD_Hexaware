package com.springboot.easypay.service;

import com.springboot.easypay.dto.PPDashboardStatsDto;
import com.springboot.easypay.dto.PayrollPolicyReqDto;
import com.springboot.easypay.model.PayrollPolicy;
import com.springboot.easypay.repository.PayrollPolicyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PayrollPolicyService {

    private final PayrollPolicyRepository payrollPolicyRepository;

    public void addPayrollPolicy(PayrollPolicyReqDto dto) {
        log.info("Adding new Payroll Policy for range {} to {}", dto.minSalary(), dto.maxSalary());

        PayrollPolicy policy = new PayrollPolicy();
        policy.setMinSalary(dto.minSalary());
        policy.setMaxSalary(dto.maxSalary());
        policy.setTaxPercentage(dto.taxPercentage());
        policy.setHraPercentage(dto.hraPercentage());
        policy.setStandardDeduction(dto.standardDeduction());

        payrollPolicyRepository.save(policy);
        log.info("Payroll Policy added successfully.");
    }

    public List<PayrollPolicy> getAllPolicies() {
        return payrollPolicyRepository.findAll();
    }

    public void updatePayrollPolicy(Long id, PayrollPolicyReqDto dto) {
        log.info("Updating Payroll Policy with ID: {}", id);


        PayrollPolicy policy = payrollPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll Policy not found with id: " + id));


        policy.setMinSalary(dto.minSalary());
        policy.setMaxSalary(dto.maxSalary());
        policy.setTaxPercentage(dto.taxPercentage());
        policy.setHraPercentage(dto.hraPercentage());
        policy.setStandardDeduction(dto.standardDeduction());

        payrollPolicyRepository.save(policy);

        log.info("Payroll Policy updated successfully for ID: {}", id);
    }

}
