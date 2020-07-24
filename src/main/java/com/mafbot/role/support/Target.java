package com.mafbot.role.support;

import com.mafbot.role.Role;
import com.mafbot.user.User;

public class Target {
    private Role role;
    private User user;

    public Target(Role role, User user) {
        this.role = role;
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }
}
