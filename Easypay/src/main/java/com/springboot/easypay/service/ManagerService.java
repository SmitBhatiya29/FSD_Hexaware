package com.springboot.easypay.service;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.LeaveRequest;
import com.springboot.easypay.repository.EmployeeRepository;
import com.springboot.easypay.repository.ManagerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestService leaveRequestService;
    public Employee getByManagerId(Long id) {
        if(id == null) return null;
        return managerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager Id doesnot exist "));
    }

    public PagedEmployeeResponseDto getEmployees(String loggedInEmail, int size, int page) {
        log.info("Fetching paginated employees for manager email: {}, page: {}", loggedInEmail, page);
        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeofManagerDb> dbPage = managerRepository.getEmployees(manager.getId(),pageable);
        Page<EmployeesOfManagerResDto> employeesPage = dbPage.map(emp -> {
            long tenure = 0;
            return new EmployeesOfManagerResDto(
                    emp.firstName(),
                    emp.lastName(),
                    emp.department(),
                    emp.designation(),
                    emp.email(),
                    emp.role(),
                    tenure
            );
        });
        return new PagedEmployeeResponseDto(
                employeesPage.getContent(),
                employeesPage.getTotalElements(),
                employeesPage.getTotalPages()
        );
    }

    public List<LeaveReqListOfManagerResDto> getLeaveReq( String loggedInEmail,String status) {
        log.info("Fetching leave requests for manager email: {}, filter status: {}", loggedInEmail, status);
        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        long managerId = manager.getId();
        List<LeaveRequest> list = null;
        if(status != null){
            RequestStatus rs = RequestStatus.valueOf(status);
            System.out.println(status);
            System.out.println(rs);
            list = leaveRequestService.getLeaveRequestOfManagerByStatus(managerId, rs);
        }else{
            list =  leaveRequestService.getLeaveRequestOfManager(managerId);
        }
        return list.stream()
                .map(Dto-> new LeaveReqListOfManagerResDto(
                        Dto.getId(),
                        Dto.getEmployee().getFirstName(),
                        Dto.getLeaveType(),
                        Dto.getStatus(),
                        Dto.getStartDate(),
                        Dto.getEndDate(),
                        (int) ChronoUnit.DAYS.between(Dto.getStartDate(), Dto.getEndDate()) + 1,
                        Dto.getDetails()
                )).toList();
    }

    public void updateLeaveReqest(ManagerLeaveUpdateDto dto,String loggedInEmail) {
        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        leaveRequestService.updateLeaveReqest(dto,manager);
    }
}
