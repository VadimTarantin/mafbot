package com.mafbot.role.support;

import com.mafbot.role.Role;

public class Target {
    private Role role;
    private String name;

    public Target(Role role, String name) {
        this.role = role;
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
