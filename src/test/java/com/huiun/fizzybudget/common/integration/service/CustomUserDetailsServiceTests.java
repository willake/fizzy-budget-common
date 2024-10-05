package com.huiun.fizzybudget.common.integration.service;

import com.huiun.fizzybudget.common.entity.Role;
import com.huiun.fizzybudget.common.entity.User;
import com.huiun.fizzybudget.common.repository.RoleRepository;
import com.huiun.fizzybudget.common.repository.UserRepository;
import com.huiun.fizzybudget.common.security.CustomUserDetails;
import com.huiun.fizzybudget.common.security.JWTAuthenticationFilter;
import com.huiun.fizzybudget.common.security.JWTTokenProvider;
import com.huiun.fizzybudget.common.service.CustomUserDetailsService;
import com.huiun.fizzybudget.common.utility.TestEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomUserDetailsServiceTests {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceTests.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    CustomUserDetailsService userDetailsService;

    private User testUser;

    private Role userRole;
    private Role managerRole;

    @BeforeEach
    public void setUp() {
        List<Role> roles = TestEntityFactory.createDefaultRoles();
        userRole = roleRepository.save(roles.get(0));
        managerRole = roleRepository.save(roles.get(1));

        testUser = TestEntityFactory.createDefaultUser(roles);
        testUser= userRepository.save(testUser);
    }

    @Test
    @Transactional
    public void testLoadUserByUsername() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getUsername());

//        Should also create a role automatically
        assertEquals(testUser.getUsername(), userDetails.getUsername());
        userDetails.getAuthorities().forEach(authority -> logger.info(authority.getAuthority()));
    }
}
