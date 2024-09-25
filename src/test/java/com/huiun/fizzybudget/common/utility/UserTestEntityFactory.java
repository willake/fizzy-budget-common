package com.huiun.fizzybudget.common.utility;

import com.huiun.fizzybudget.common.entities.Role;
import com.huiun.fizzybudget.common.entities.User;

import java.util.Arrays;
import java.util.List;

public class UserTestEntityFactory {

    public static User createDefaultUser(List<Role> roles) {
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("testUser");
        user.setEmail("testUser@gmail.com");
        user.setActivated(true);

        roles.forEach(role -> user.getRoles().add(role));

        return user;
    }

    // first one is user role and the second one is manager role
    public static List<Role> createDefaultRoles() {
        Role userRole = new Role();
        userRole.setRoleName("ROLE_USER");

        Role managerRole = new Role();
        managerRole.setRoleName("ROLE_MANAGER");

        return Arrays.asList(
                userRole, managerRole
        );
    }
}
