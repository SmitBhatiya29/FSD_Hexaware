package com.springboot.easypay.repository;

import com.springboot.easypay.dto.StatsOFHrGroupByDto;
import com.springboot.easypay.enums.RequestStatus;
import com.springboot.easypay.enums.Role;
import com.springboot.easypay.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface HrRepository extends JpaRepository<Employee, Long> {
    @Query("""
    SELECT e From Employee e
    Join User u on e.user.id = u.id
    where u.role = "MANAGER"
""")
    List<Employee> getAllManager();

    @EntityGraph(attributePaths = {"user"})
    @Query("""
        SELECT e FROM Employee e 
        WHERE e.status = ?1
        AND (?2 IS NULL OR e.user.role = ?2) 
        AND (?3 IS NULL OR e.department = ?3)
    """)
    Page<Employee> findByStatus(RequestStatus requestStatus, Role role, String department, Pageable pageable);

    @Query("""
        SELECT e FROM Employee e 
        WHERE (?1 IS NULL OR e.user.role = ?1) 
        AND (?2 IS NULL OR e.department = ?2)
    """)
    Page<Employee> allEmployeeByFilter(Role role, String department, Pageable pageable);

    @Query("""
    select new com.springboot.easypay.dto.StatsOFHrGroupByDto(e.department,count(e.id))
    FROM Employee  e 
    where e.status = "APPROVED"
    group by e.department
""")
    List<StatsOFHrGroupByDto> getDepartmentOverview();

}
