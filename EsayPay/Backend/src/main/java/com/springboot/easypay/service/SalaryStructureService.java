package com.springboot.easypay.service;

import com.springboot.easypay.dto.SalaryStructureReqDto;
import com.springboot.easypay.dto.SalaryStructureResDto;
import com.springboot.easypay.dto.SalaryStructureUpdateReqDto;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.PayrollPolicy;
import com.springboot.easypay.model.SalaryStructure;
import com.springboot.easypay.repository.PayrollPolicyRepository;
import com.springboot.easypay.repository.SalaryStructureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
@Slf4j
public class SalaryStructureService {
    private final SalaryStructureRepository salaryStructureRepository;
    private final EmployeeService employeeService;
    private final PayrollPolicyRepository payrollPolicyRepository;

    public void addSalaryStructure(SalaryStructureReqDto salaryStructureReqDto) {
        log.info("Adding salary structure for employee ID: {}", salaryStructureReqDto.employeeId());

        Employee employee = employeeService.getEmployeeByEmployeeId(salaryStructureReqDto.employeeId());

        if (salaryStructureRepository.getSalaryStructureByEmployeeId(employee.getId()) != null) {
            throw new RuntimeException("Salary structure already exists for this employee.");
        }

        BigDecimal monthlyBasic = salaryStructureReqDto.basicSalary();

        BigDecimal yearlyBasic = monthlyBasic.multiply(BigDecimal.valueOf(12));


        PayrollPolicy policy = payrollPolicyRepository.findPolicyBySalaryRange(yearlyBasic)
                .orElseThrow(() -> new RuntimeException("No payroll policy defined for this salary range!"));


        // HRA = (Basic * hraPercentage) / 100
        BigDecimal hraAllowance = monthlyBasic
                .multiply(policy.getHraPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Standard Deduction usually fixed per month (Yearly Deduction / 12)
        BigDecimal monthlyStandardDeduction = policy.getStandardDeduction()
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        //  save to db
        SalaryStructure salaryStructure = new SalaryStructure();
        salaryStructure.setEmployee(employee);
        salaryStructure.setBasicSalary(monthlyBasic);
        salaryStructure.setHraAllowance(hraAllowance);
        salaryStructure.setStandardDeduction(monthlyStandardDeduction);
        salaryStructure.setTaxPercentage(policy.getTaxPercentage()); // Direct percentage

        salaryStructureRepository.save(salaryStructure);
        log.info("Automated Salary structure added successfully for employee ID: {}", employee.getId());
    }



    public SalaryStructureResDto getSalaryStructure(long employeeId) {
        return salaryStructureRepository.getSalaryStructure(employeeId);
    }

    public void updateSalaryStucture(SalaryStructureUpdateReqDto salaryStructureUpdateReqDto, Long employeeId) {
        log.info("Updating salary structure for employee ID: {}", employeeId);
        employeeService.getEmployeeByEmployeeId(employeeId);
        SalaryStructure salaryStructure = salaryStructureRepository.getSalaryStructureByEmployeeId(employeeId);
        if (salaryStructure == null) {
            log.error("Salary structure not found for employee ID: {}", employeeId);
            throw new RuntimeException("Salary structure not found for this employee");
        }
        salaryStructure.setBasicSalary(salaryStructureUpdateReqDto.basicSalary());
        salaryStructure.setHraAllowance(salaryStructureUpdateReqDto.hraAllowance());
        salaryStructure.setStandardDeduction(salaryStructureUpdateReqDto.standardDeduction());
        salaryStructure.setTaxPercentage(salaryStructureUpdateReqDto.taxPercentage());

        salaryStructureRepository.save(salaryStructure);

        log.info("Salary structure updated successfully for employee ID: {}", employeeId);
    }


}
