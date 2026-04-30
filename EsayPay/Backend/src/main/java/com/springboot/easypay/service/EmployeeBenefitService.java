package com.springboot.easypay.service;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.mapper.EmployeeBenifitMapper;
import com.springboot.easypay.model.Benefit;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.EmployeeBenefit;
import com.springboot.easypay.repository.BenefitRepository;
import com.springboot.easypay.repository.EmployeeBenefitRepository;
import com.springboot.easypay.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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


        EmployeeBenefit employeeBenefit = employeeBenefitRepository.findById(employeeBenefitReqDto.benefitId())
                .orElseThrow(()-> new ResourceNotFoundException("Employee Request not foound"));


        Benefit benefit = benefitRepository
                .findById(employeeBenefit.getBenefit().getId())
                .orElseThrow(() -> {
                    log.error("Benefit ID {} is invalid", employeeBenefitReqDto.benefitId());
                    return new RuntimeException("Benefit Id Invalid");
                });

        if(employeeBenefitReqDto.status() == RequestStatus.APPROVED){
            employeeBenefit.setBenefitAmount(employeeBenefitReqDto.benefitAmount());
        }

        employeeBenefit.setStatus(employeeBenefitReqDto.status());

        employeeBenefitRepository.save(employeeBenefit);

        log.info("Successfully assigned benefit ID {} to employee ID {}", benefit.getId(), employee.getId());
    }

    public void requestBenefit(Long benefitId, String loggedInEmail) {
        log.info("New benefit request. Benefit ID: {}, User: {}", benefitId, loggedInEmail);

        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);
        Benefit benefit = benefitRepository.findById(benefitId)
                .orElseThrow(() -> {
                    log.error("Request failed. Benefit ID {} not found", benefitId);
                    return new ResourceNotFoundException("Benefit is not found");
                });

        boolean alreadyRequested = employeeBenefitRepository.existsByEmployeeAndBenefit(employee, benefit);
        if (alreadyRequested) {

            throw new IllegalStateException("You have already requested this benefit.");
        }
        EmployeeBenefit employeeBenefit = new EmployeeBenefit();
        employeeBenefit.setEmployee(employee);
        employeeBenefit.setBenefit(benefit);
        employeeBenefit.setStatus(RequestStatus.PENDING);

        employeeBenefitRepository.save(employeeBenefit);

        log.info("Benefit requested successfully for Employee ID: {}", employee.getId());
    }

    public BenefitReqPagResDto getMyBenefitReqest(String loggedInEmail, int page, int size) {
        log.info("Fetching benefit requests for: {}", loggedInEmail);

        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);

        Pageable pageable =  PageRequest.of(page, size);
        Page<EmployeeBenefit> pageEmplBenefit =  employeeBenefitRepository.getMyBenefitReqest(employee.getId(),pageable);
        long totalRecords = pageEmplBenefit.getTotalElements();
        int totalPages = pageEmplBenefit.getTotalPages();

        log.info("Found {} benefit requests for Employee ID: {}", pageEmplBenefit.getTotalElements(), employee.getId());

        List<EmployeeBenefitReqListDto> list = pageEmplBenefit.stream().map(EmployeeBenifitMapper :: mapToDto).toList();

        return new BenefitReqPagResDto(
                list,
                totalRecords,
                totalPages
        );

    }

    public void deleteBenefitRequest(Long requestId, String loggedInEmail) {
        log.info("Attempting to delete request ID: {}", requestId);

        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);
        EmployeeBenefit request = employeeBenefitRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Benefit request not found"));

        if (!request.getEmployee().getId().equals(employee.getId())) {
            throw new IllegalStateException("You are not authorized to delete this request.");
        }
        employeeBenefitRepository.delete(request);
        log.info("Request ID {} deleted successfully", requestId);
    }

    public AllBenefitResDto getAllBenefitReq(int page, int size) {
        log.info("Admin fetching all benefit requests. Page: {}", page);

        Pageable pageable =  PageRequest.of(page, size);
        Page<EmployeeBenefit> pageEmployeeBenefit =  employeeBenefitRepository.findAll(pageable);
        long totalRecords = pageEmployeeBenefit.getTotalElements();
        int totalPages = pageEmployeeBenefit.getTotalPages();
        List<PendingBenefitResDto> data = pageEmployeeBenefit.stream().map(EmployeeBenifitMapper :: mapTogetAllResDto).toList();
        return new AllBenefitResDto(data,totalRecords,totalPages);

    }

    public void revokeBenefit(Long id) {
        log.info("Revoking benefit ID: {}", id);

        log.info("Payroll Processor is revoking benefit ID: {}", id);
        EmployeeBenefit request = employeeBenefitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benefit request not found"));

        employeeBenefitRepository.delete(request);
        log.info("Successfully revoked and deleted benefit ID: {}", id);
    }

    public int getPendingBenefitReq(String loggedInEmail) {
        log.info("Counting pending benefits for: {}", loggedInEmail);

        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);
        return employeeBenefitRepository.getPendingBenefitReq(employee.getId(),RequestStatus.PENDING);
    }

    public BenefitReqPagResDto getBenefitRequestByEmployeeId(Long employeeId, int page, int size) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Pageable pageable = PageRequest.of(page, size);


        Page<EmployeeBenefit> pageEmplBenefit = employeeBenefitRepository.getMyBenefitReqest(employee.getId(), pageable);

        long totalRecords = pageEmplBenefit.getTotalElements();
        int totalPages = pageEmplBenefit.getTotalPages();

        log.info("Found {} benefit requests for Employee ID: {}", totalRecords, employeeId);

        List<EmployeeBenefitReqListDto> list = pageEmplBenefit.stream()
                .map(EmployeeBenifitMapper::mapToDto)
                .toList();

        return new BenefitReqPagResDto(
                list,
                totalRecords,
                totalPages
        );
    }
}
