package com.service;

import com.model.CartItem;
import com.model.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;
    public void addUser(User user) throws SQLException {
        userRepository.addUser(user);
    }


}
