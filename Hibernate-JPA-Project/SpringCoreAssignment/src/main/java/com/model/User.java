package com.model;

import com.enums.UserMembership;
import org.springframework.stereotype.Component;

@Component
public class User {
    private int id;
    private String name;
    private UserMembership membership;

    public User() {
    }

    public User(int id, String name, UserMembership membership) {
        this.id = id;
        this.name = name;
        this.membership = membership;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserMembership getMembership() {
        return membership;
    }

    public void setMembership(UserMembership membership) {
        this.membership = membership;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", membership=" + membership +
                '}';
    }
}
