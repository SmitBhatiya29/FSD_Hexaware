package com.springboot.easypay.repository;
import com.springboot.easypay.dto.EmployeeofManagerDb;
import com.springboot.easypay.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface ManagerRepository extends JpaRepository<Employee,Long> {



    @Query("""
        SELECT new com.springboot.easypay.dto.EmployeeofManagerDb(
            e.firstName, 
            e.lastName, 
            e.department, 
            e.designation, 
            u.userEmail, 
            u.role,
            e.joiningDate
        )
        FROM Employee e
        JOIN e.user u
        WHERE e.manager.id = :id
    """)
    Page<EmployeeofManagerDb> getEmployees(long id, Pageable pageable);
}
