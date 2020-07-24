package com.mafbot.service.role;

import com.mafbot.user.User;

import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    @Override
    public List<User> getUsersWithActiveRoles(List<User> users) {
        List<User> result = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().isActive()) {
                result.add(user);
            }
        }
        return result;
    }
}