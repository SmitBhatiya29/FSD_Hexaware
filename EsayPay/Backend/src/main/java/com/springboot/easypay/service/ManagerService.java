package com.springboot.easypay.service;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.LeaveRequest;
import com.springboot.easypay.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
@Slf4j
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestService leaveRequestService;
    private final TimeSheetRepository timeSheetRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PayrollRecordRepository payrollRecordRepository;

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
            if (emp.hireDate() != null) {
                tenure = ChronoUnit.YEARS.between(emp.hireDate(), LocalDate.now());
            }
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

    public PageLeaveReqListOfManagerResDto getLeaveReq( String loggedInEmail,String status,int page, int size) {
        log.info("Fetching leave requests for manager email: {}, filter status: {}", loggedInEmail, status);
        Pageable pageable =  PageRequest.of(page, size);

        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        long managerId = manager.getId();
        Page<LeaveRequest> list = null;
        if(status != null){
            RequestStatus rs = RequestStatus.valueOf(status);
            System.out.println(status);
            System.out.println(rs);
            list = leaveRequestService.getLeaveRequestOfManagerByStatus(managerId, rs,pageable);
        }else{
            list =  leaveRequestService.getLeaveRequestOfManager(managerId,pageable);
        }
        List<LeaveReqListOfManagerResDto> data =  list.stream()
                .map(Dto-> new LeaveReqListOfManagerResDto(
                        Dto.getId(),
                        Dto.getEmployee().getFirstName() + " " +Dto.getEmployee().getLastName(),
                        Dto.getLeaveType(),
                        Dto.getStatus(),
                        Dto.getStartDate(),
                        Dto.getEndDate(),
                        (int) ChronoUnit.DAYS.between(Dto.getStartDate(), Dto.getEndDate()) + 1,
                        Dto.getDetails()
                )).toList();



        return new PageLeaveReqListOfManagerResDto(data,
                list.getTotalElements(),
                list.getTotalPages());
    }

    public void updateLeaveReqest(ManagerLeaveUpdateDto dto,String loggedInEmail) {
        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);
        leaveRequestService.updateLeaveReqest(dto,manager);
    }

    public ManagerStatResDto getStats(String loggedInEmail) {
        log.info("Loading dashboard stats for manager: {}", loggedInEmail);

        Employee Manager = employeeRepository.findByUser_UserEmail(loggedInEmail);

        int teamSize = managerRepository.getTeamSize(Manager.getId());
        int countPendingLeaveReq = leaveRequestRepository.getCountPendingLeaveReq(Manager.getId());
        int countPendingTimeSheet = timeSheetRepository.getCountPendingTimeSheet(Manager.getId());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
        String formattedDate = currentDate.format(formatter);

        BigDecimal totalTeamPayStub = payrollRecordRepository.getTotalTeamPayStub(formattedDate);

        log.info("Stats summary - Team: {}, Leave Pending: {}, PayStub: {}", teamSize, countPendingLeaveReq, totalTeamPayStub);
        return new ManagerStatResDto(teamSize,countPendingLeaveReq,countPendingTimeSheet,totalTeamPayStub);
    }

    public List<DepartmentByOverviewPaystubResDto> getDepartmentByOverviewPaystub(String loggedInEmail) {
        log.info("Fetching dept paystub overview for manager: {}", loggedInEmail);

        Employee manager = employeeRepository.findByUser_UserEmail(loggedInEmail);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
        String formattedDate = currentDate.format(formatter);

        return payrollRecordRepository.getDepartmentByOverviewPaystub(manager.getId(),formattedDate);
    }
}
