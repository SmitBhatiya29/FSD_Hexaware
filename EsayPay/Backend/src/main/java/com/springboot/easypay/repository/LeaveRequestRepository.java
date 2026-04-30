package com.springboot.easypay.repository;

import com.springboot.easypay.enums.LeaveType;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("""
    select lr from LeaveRequest lr
    where lr.employee.id = ?1
    AND (?2 IS NULL OR lr.status = ?2)
    AND (?3 IS NULL OR lr.leaveType = ?3)
""")
    List<LeaveRequest> getLeaveRequest(long employeeId, RequestStatus status, LeaveType leaveType);

    @Query("""
    select lr from LeaveRequest lr
    where lr.employee.id = ?1
    AND (?2 IS NULL OR lr.status = ?2)
    AND (?3 IS NULL OR lr.leaveType = ?3)
""")
    Page<LeaveRequest> getLeaveRequestPage(long employeeId, RequestStatus status, LeaveType leaveType,Pageable pageable);

    @Query("""
    select lr from LeaveRequest lr
    where lr.employee.manager.id = ?1
""")
    Page<LeaveRequest> getLeaveRequestOfManager(long managerId,Pageable pageable);

    @Query("""
    select lr from LeaveRequest lr
    where lr.employee.manager.id = ?1
    and (?2 is null or lr.status = ?2)
""")
    Page<LeaveRequest> getLeaveRequestOfManagerByStatus(long managerId, RequestStatus rs, Pageable pageable);

    @Query("""
    select count(lr.id) from LeaveRequest lr
    where lr.employee.manager.id = ?1 and lr.status = "PENDING"
    """)
    int getCountPendingLeaveReq(Long id);
}
