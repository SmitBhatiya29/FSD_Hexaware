package com.springboot.easypay.service;

import com.springboot.easypay.dto.*;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.EmployeeKyc;
import com.springboot.easypay.model.LeaveRequest;
import com.springboot.easypay.model.User;
import com.springboot.easypay.repository.EmployeeKycRepository;
import com.springboot.easypay.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EmployeeService {
    private  final EmployeeRepository employeeRepository;
    private final EmployeeKycRepository employeeKycRepository;
    private final LeaveRequestService leaveRequestService;
    private final UserService userService;


    public void signup(SignupDto signupDto) {
        log.info("Initiating signup for user email: {}", signupDto.email());
        userService.addUser(signupDto);
        log.info("Signup successful for user email: {}", signupDto.email());
    }
    public void addKycInfo(String loggedInEmail, EmployeeKycInfoDto dto) {
        log.info("Adding KYC info for employee email: {}", loggedInEmail);

        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);

        EmployeeKyc employeeKyc = new EmployeeKyc();
        employeeKyc.setEmployee(employee);
        employeeKyc.setAadharNo(dto.aadharNo());
        employeeKyc.setPanNo(dto.panNo());
        employeeKyc.setBankAccountNo(dto.bankAccountNo());
        employeeKyc.setIfscCode(dto.ifscCode());
        employeeKyc.setCurrentAddress( dto.currentAddress());
        employeeKycRepository.save(employeeKyc);

        log.info("KYC info saved successfully for employee ID: {}", employee.getId());
    }

    public List<LeaveReqOfEmpResDto> getLeaveRequest(String loggedInEmail) {
        log.info("Fetching leave requests for employee email: {}", loggedInEmail);

        Employee employee = employeeRepository.findByUser_UserEmail(loggedInEmail);
        List<LeaveRequest> list = leaveRequestService.getLeaveRequest(employee.getId());
        log.info("Found {} leave requests for employee ID: {}", list.size(), employee.getId());
        return list.stream()
                        .map(Dto-> new LeaveReqOfEmpResDto(
                                Dto.getId(),
                                Dto.getLeaveType(),
                                Dto.getStatus(),
                                Dto.getStartDate(),
                                Dto.getEndDate(),
                                (int) ChronoUnit.DAYS.between(Dto.getStartDate(), Dto.getEndDate()) + 1,
                                Dto.getDetails()
                        )).toList();

    }

    public void updateEmployeePassword(EmployeePasswordUpdateDto dto) {
        log.info("Updating password for employee ID: {}", dto.empId());
        Employee employee = employeeRepository.findById(dto.empId())
                .orElseThrow(() ->{
                    log.error("Failed to update password: Employee ID {} is invalid", dto.empId());
                    return new ResourceNotFoundException("Employee id is invalid");
                });


        userService.updateEmployeePassword(dto,employee.getUser().getId());


        log.info("Password updated successfully for employee ID: {}", dto.empId());
    }


    public Employee getEmployeeByEmployeeId(long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(()-> new ResourceNotFoundException("Employee id is invalid"));
    }



    public void submitEmployeeDetails(EmployeeProfileSubmitDto profileDto, String loggedInEmail) {
        log.info("Submitting profile details for email: {}", loggedInEmail);
        //1.get user
        User user = userService.findByUserEmail(loggedInEmail);

        if(user == null){
            log.error("User not found for email: {}", loggedInEmail);
            throw new ResourceNotFoundException("User not found");
        }

        // 2. check if employee profile already exists
        if(employeeRepository.existsByUser(user)){
            log.warn("Profile already submitted for user ID: {}", user.getId());
            throw new RuntimeException("Profile already submitted!");
        }

        //3.create
        Employee employee = new Employee();
        employee.setUser(user);
        employee.setFirstName(profileDto.firstName());
        employee.setLastName(profileDto.lastName());
        employee.setDepartment(profileDto.department());
        employee.setDesignation(profileDto.designation());
        employee.setJoiningDate(LocalDate.now()); //HR baad mein update kar dega
        employee.setStatus(RequestStatus.PENDING);
        employeeRepository.save(employee);

        log.info("Employee details submitted successfully for user ID: {}", user.getId());
    }
}
