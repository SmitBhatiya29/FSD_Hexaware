package com.springboot.easypay.service;

import com.springboot.easypay.dto.EmployeePasswordUpdateDto;
import com.springboot.easypay.dto.SignupDto;
import com.springboot.easypay.enums.Role;
import com.springboot.easypay.exception.ResourceNotFoundException;
import com.springboot.easypay.model.User;
import com.springboot.easypay.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);
    }

    public void updateEmployeePassword(EmployeePasswordUpdateDto dto,Long userId) {
        //1.check user id is available or valid
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Id is invalid "));

        //2. check old password true or not
        if(!passwordEncoder.matches(dto.oldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password doesn't match");
        }

        //3.set new password
        String encodedNewPassword = passwordEncoder.encode(dto.newPassword());
        user.setPasswordHash(encodedNewPassword);


        userRepository.save(user);

    }

    public User findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);

    }

    public void addUser(SignupDto signupDto) {
        User user = new User();
        user.setUserEmail(signupDto.email());
        user.setPasswordHash(passwordEncoder.encode(signupDto.password()));
        user.setRole(Role.EMPLOYEE); //default role
        user.setActive(false); //hr will update
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
