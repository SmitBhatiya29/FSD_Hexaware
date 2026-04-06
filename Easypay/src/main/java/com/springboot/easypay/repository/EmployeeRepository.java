package com.springboot.easypay.repository;

import com.springboot.easypay.model.Employee;
import com.springboot.easypay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
    select e from Employee e
    where e.user.userEmail=?1
"""
    )
    Employee findByUser_UserEmail(String email);

    @Query("""
    select e from Employee e
    where e.user.id = ?1
""")
    Employee getEmployeeByUserId(int userId);


    boolean existsByUser(User user);
}
