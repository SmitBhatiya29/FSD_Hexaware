package com.repository;

import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, membership) VALUES (?,?)";
        jdbcTemplate.update(sql, user.getName(), user.getMembership().toString());
        return;
    }
}
