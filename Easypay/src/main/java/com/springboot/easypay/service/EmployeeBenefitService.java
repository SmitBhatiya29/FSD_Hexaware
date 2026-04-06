package com.springboot.easypay.service;

import com.springboot.easypay.dto.EmployeeBenefitReqDto;
import com.springboot.easypay.model.Benefit;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.EmployeeBenefit;
import com.springboot.easypay.repository.BenefitRepository;
import com.springboot.easypay.repository.EmployeeBenefitRepository;
import com.springboot.easypay.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeBenefitService {
    private final EmployeeBenefitRepository employeeBenefitRepository;
    private final EmployeeRepository employeeRepository;
    private final BenefitRepository benefitRepository;

    public void assignBenefit(EmployeeBenefitReqDto employeeBenefitReqDto,String loggedInEmail) {
        log.info("Assigning benefit ID {} to employee email: {}", employeeBenefitReqDto.benefitId(), loggedInEmail);
        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);

        Benefit benefit = benefitRepository
                .findById(employeeBenefitReqDto.benefitId())
                .orElseThrow(() -> {
                    log.error("Benefit ID {} is invalid", employeeBenefitReqDto.benefitId());
                    return new RuntimeException("Benefit Id Invalid");
                });

        EmployeeBenefit employeeBenefit = new EmployeeBenefit();
        employeeBenefit.setEmployee(employee);
        employeeBenefit.setBenefit(benefit);
        employeeBenefit.setBenefitAmount(employeeBenefitReqDto.benefitAmount());

        employeeBenefitRepository.save(employeeBenefit);

        log.info("Successfully assigned benefit ID {} to employee ID {}", benefit.getId(), employee.getId());
    }
}
