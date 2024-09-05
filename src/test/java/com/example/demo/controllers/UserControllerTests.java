package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTests {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private CreateUserRequest createTestUserRequest;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        createTestUserRequest = TestUtils.createTestUserRequest("tolm", "123456789", "123456789");
    }

    @Test
    public void createUser() {
        when(encoder.encode("123456789")).thenReturn("123456789");
        final ResponseEntity<User> response = userController.createUser(createTestUserRequest);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("tolm", user.getUsername());
        assertEquals("123456789", user.getPassword());
    }

    @Test
    public void createUserFailedByPasswordLessThan7Words() {
        when(encoder.encode("123456789")).thenReturn("123456789");
        createTestUserRequest.setPassword("123456");
        createTestUserRequest.setConfirmPassword("123456");
        final ResponseEntity<User> response = userController.createUser(createTestUserRequest);

        assertNull(response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserFailedByPasswordAndConfirmPasswordNotEqual() {
        when(encoder.encode("123456789")).thenReturn("123456789");
        createTestUserRequest.setConfirmPassword("12345678910");

        final ResponseEntity<User> response = userController.createUser(createTestUserRequest);

        assertNull(response.getBody());
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserById() {
        User user = TestUtils.createTestUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findUserByIdFailedByNotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        ResponseEntity<User> response = userController.findById(1L);

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findUserByName() {
        User user = TestUtils.createTestUser();
        when(userRepository.findByUsername("tolm")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("tolm");

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findUserByUserNameFailedByNotFound(){
        when(userRepository.findByUsername("tolm")).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName("tolm");

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }
}
