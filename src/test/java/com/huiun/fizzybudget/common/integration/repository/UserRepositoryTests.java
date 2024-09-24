package com.huiun.fizzybudget.common.integration.repository;

import com.huiun.fizzybudget.common.entities.Role;
import com.huiun.fizzybudget.common.entities.User;
import com.huiun.fizzybudget.common.repository.RoleRepository;
import com.huiun.fizzybudget.common.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User testUser;

    private Role userRole;
    private Role managerRole;

    @BeforeEach
    public void setUp() {
        userRole = new Role();
        userRole.setRoleName("ROLE_USER");
        userRole = roleRepository.save(userRole);

        managerRole = new Role();
        managerRole.setRoleName("ROLE_MANAGER");
        managerRole = roleRepository.save(managerRole);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPasswordHash("testUser");
        testUser.setEmail("testUser@gmail.com");
        testUser.setActivated(true);

        testUser.getRoles().add(userRole);

        userRepository.save(testUser);
    }

    @Test
    public void testSaveAndFindById() {
        User user = new User();
        user.setUsername("newUser");
        user.setPasswordHash("newUser");
        user.setEmail("newUser@gmail.com");
        user.setActivated(true);

        User createdUser = userRepository.save(user);
        assertNotNull(createdUser);
        assertEquals("newUser", createdUser.getUsername());

        Optional<User> retrievedUser = userRepository.findById(createdUser.getUserId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("newUser", retrievedUser.get().getUsername());
    }

    @Test
    public void testUserRoleRelationship() {
        testUser.getRoles().add(managerRole);

        User savedUser = userRepository.save(testUser);

        assertEquals(2, savedUser.getRoles().size());
        assertTrue(savedUser.getRoles().stream()
                .anyMatch(role -> managerRole.getRoleName().equals(role.getRoleName())));
    }
}
