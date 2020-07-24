package com.mafbot.service.role;

import com.mafbot.user.User;

import java.util.List;

public interface RoleService {
    List<User> getUsersWithActiveRoles(List<User> users);
}
