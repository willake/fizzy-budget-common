package com.huiun.fizzybudget.common.integration.repository;

import com.huiun.fizzybudget.common.entity.Role;
import com.huiun.fizzybudget.common.entity.User;
import com.huiun.fizzybudget.common.repository.RoleRepository;
import com.huiun.fizzybudget.common.repository.UserRepository;
import com.huiun.fizzybudget.common.utility.UserTestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
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
        List<Role> roles = UserTestEntityFactory.createDefaultRoles();
        userRole = roles.getFirst();
        managerRole = roles.get(1);
        userRole = roleRepository.save(userRole);
        managerRole = roleRepository.save(managerRole);


        testUser = UserTestEntityFactory.createDefaultUser(roles);
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

        Optional<User> retrievedUser = userRepository.findById(createdUser.getId());
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
