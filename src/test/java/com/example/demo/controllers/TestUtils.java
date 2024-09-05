package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }

            f.set(target, toInject);

            if (wasPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("tolm");
        user.setPassword("123456789");
        user.setCart(createTestUserCart(user, createTestItems()));

        return user;
    }

    public static List<Item> createTestItems() {
        List<Item> items = new ArrayList<>();
        Item item1 = createTestItem1();
        Item item2 = createTestItem2();
        items.add(item1);
        items.add(item2);

        return items;
    }

    public static Item createTestItem1() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setName("Round Widget");
        item.setDescription("A widget that is round");

        return item;
    }

    public static Item createTestItem2() {
        Item item = new Item();
        item.setId(2L);
        item.setPrice(BigDecimal.valueOf(1.99));
        item.setName("Square Widget");
        item.setDescription("A widget that is square");

        return item;
    }

    private static Cart createTestUserCart(User user, List<Item> items) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(4.98));

        return cart;
    }

    public static UserOrder createTestUserOrder(User user) {
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setTotal(user.getCart().getTotal());
        userOrder.setItems(user.getCart().getItems());

        return userOrder;
    }

    public static ModifyCartRequest createTestModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("tolm");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(50);

        return modifyCartRequest;
    }

    public static CreateUserRequest createTestUserRequest(String... args) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(args[0]);
        createUserRequest.setPassword(args[1]);
        createUserRequest.setConfirmPassword(args[2]);

        return createUserRequest;
    }
}
