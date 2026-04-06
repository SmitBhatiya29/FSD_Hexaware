package com.springboot.easypay.service;

import com.springboot.easypay.dto.AllManagerDetailsResDto;
import com.springboot.easypay.dto.HRApprovalReqDto;
import com.springboot.easypay.dto.PendingEmployeeProfilePagResDto;
import com.springboot.easypay.dto.PendingEmployeeProfileResDto;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.enums.Role;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.mapper.EmployeeMapper;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.repository.HrRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class HrService {

    private final HrRepository hrRepository;
    private final LeaveBalanceService leaveBalanceService;

    public void approveEmployeeByHR(Long employeeId, HRApprovalReqDto hrApprovalDto) {
        log.info("HR approval process initiated for employee ID: {}. Status: {}", employeeId, hrApprovalDto.status());
        //find employee details
        Employee employee = hrRepository.findById(employeeId)
                .orElseThrow(() ->{
                    log.error("Employee not found with ID: {}", employeeId);
                     return new ResourceNotFoundException("Employee not found");
                });
        // already approved
        if(employee.getStatus() == RequestStatus.APPROVED) {
            log.warn("Employee ID {} is already approved", employeeId);
            throw new RuntimeException("Employee is already approved!");
        }
       //if approved by hr
        if(hrApprovalDto.status() == (RequestStatus.APPROVED)){
            if( hrApprovalDto.role().equals(Role.MANAGER)) {
                employee.setManager(null);
            } else {
                Employee Manager = hrRepository.findById(hrApprovalDto.managerId())
                        .orElseThrow(() -> {
                            log.error("Manager not found with ID: {}", hrApprovalDto.managerId());
                            return new ResourceNotFoundException("Manager not found");
                        });
                employee.setManager(Manager);
            }
            employee.setJoiningDate(hrApprovalDto.joiningDate());
            employee.setStatus(RequestStatus.APPROVED);
            employee.getUser().setActive(true);
            employee.getUser().setRole(hrApprovalDto.role());
        }else {
            employee.setStatus(RequestStatus.REJECTED);
            log.info("Employee ID {} rejected by HR", employeeId);
        }

        employee = hrRepository.save(employee);

        if(hrApprovalDto.status().equals(RequestStatus.APPROVED)){
            leaveBalanceService.addLeaveBalance(employee);
            log.info("Leave balances added for newly approved employee ID: {}", employeeId);
        }

    }


    public List<AllManagerDetailsResDto> getAllManager() {
        List<Employee> managerList= hrRepository.getAllManager();
        return managerList
                .stream()
                .map(manager -> new AllManagerDetailsResDto(
                        manager.getId(),
                        manager.getFirstName(),
                        manager.getLastName(),
                        manager.getDepartment()
                ))
                .toList();

    }

        public PendingEmployeeProfilePagResDto getAllPendingProfiles(int page, int size, Role role, String department) {
            Pageable pageable =  PageRequest.of(page, size);
            Page<Employee> pageEmployee =  hrRepository.findByStatus(RequestStatus.PENDING,role,
                    department, pageable);

            long totalRecords = pageEmployee.getTotalElements();
            int totalPages = pageEmployee.getTotalPages();
            List<PendingEmployeeProfileResDto> listDto = pageEmployee
                    .toList()
                    .stream()
                    .map(EmployeeMapper::mapToPendingProfileDto)
                    .toList();

            return new PendingEmployeeProfilePagResDto(
                    listDto,
                    totalRecords,
                    totalPages
            );
        }
}
