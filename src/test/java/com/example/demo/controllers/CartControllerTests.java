package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private User user;
    private ModifyCartRequest modifyCartRequest;

    @Before
    public void setUp() {
        cartController = new CartController();
        user = TestUtils.createTestUser();
        modifyCartRequest = TestUtils.createTestModifyCartRequest();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
    }

    @Test
    public void addToCart() {
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        Item item1 = TestUtils.createTestItem1();
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(item1));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = user.getCart();
        IntStream.range(0, modifyCartRequest.getQuantity())
                .forEach(i -> cart.addItem(item1));

        assertNotNull(response.getBody());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void addToCartFailedByUserNull(){
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(null);
        Item item1 = TestUtils.createTestItem1();
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartFailedByItemNotFound(){
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        Item item1 = TestUtils.createTestItem1();
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart() {
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        Item item1 = TestUtils.createTestItem1();
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(Optional.of(item1));
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = user.getCart();
        IntStream.range(0, modifyCartRequest.getQuantity())
                .forEach(i -> cart.removeItem(
                        item1));

        assertNotNull(response.getBody());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void removeFromCartFailedByUserNull(){
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(null);
        Item item1 = TestUtils.createTestItem1();
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartFailedByItemNotFound(){
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        Item item1 = TestUtils.createTestItem1();
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }
}
