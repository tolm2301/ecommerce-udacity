package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTests {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private User user;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        user = TestUtils.createTestUser();
    }

    @Test
    public void submit() {
        when(userRepository.findByUsername("tolm")).thenReturn(user);
        final ResponseEntity<UserOrder> response = orderController.submit("tolm");

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(userOrder.getUser(), user);
        assertEquals(userOrder.getItems(), user.getCart().getItems());
        assertEquals(userOrder.getTotal(), user.getCart().getTotal());
    }

    @Test
    public void submitFailed() {
        when(userRepository.findByUsername("tolm")).thenReturn(null);
        final ResponseEntity<UserOrder> response = orderController.submit("tolm");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void getOrdersForUser() {
        List<UserOrder> mockUserOrder = Arrays.asList(TestUtils.createTestUserOrder(user));
        when(userRepository.findByUsername("tolm")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(mockUserOrder);
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("tolm");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        List<UserOrder> userOrders = response.getBody();

        assertFalse(userOrders.isEmpty());
        assertEquals(mockUserOrder, userOrders);
    }

    @Test
    public void getOrderForUserFailed() {
        when(userRepository.findByUsername("tolm")).thenReturn(null);
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("tolm");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
