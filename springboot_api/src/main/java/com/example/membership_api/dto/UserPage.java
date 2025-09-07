package com.example.membership_api.dto;

import com.example.membership_api.model.User;
import java.util.List;

public class UserPage {
    private List<User> users;
    private Long total;

    public UserPage(List<User> users, Long total) {
        this.users = users;
        this.total = total;
    }

    public List<User> getUsers() {
        return users;
    }

    public Long getTotal() {
        return total;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
