package com.springboot.easypay.repository;

import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("""
    select lr from LeaveRequest lr
    where lr.employee.id = ?1
""")
    List<LeaveRequest> getLeaveRequest(long employeeId);

    @Query("""
    select lr from LeaveRequest lr
    where lr.employee.manager.id = ?1
""")
    List<LeaveRequest> getLeaveRequestOfManager(long managerId);

    @Query("""
    select lr from LeaveRequest lr
    where lr.employee.manager.id = ?1
    and (?2 is null or lr.status = ?2)
""")
    List<LeaveRequest> getLeaveRequestOfManagerByStatus(long managerId, RequestStatus rs);
}
