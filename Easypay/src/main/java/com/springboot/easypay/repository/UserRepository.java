package com.springboot.easypay.repository;

import com.springboot.easypay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
select u from User u where u.userEmail = ?1
""")
    UserDetails getUserByUsername(String username);


    boolean existsByUserEmail(String email);

    @Query("""
select u from User u
where u.userEmail = ?1
""")
    User findByUserEmail(String userEmail);
}
