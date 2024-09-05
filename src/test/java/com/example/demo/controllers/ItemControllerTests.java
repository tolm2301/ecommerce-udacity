package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        when(itemRepository.findAll()).thenReturn(TestUtils.createTestItems());
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertFalse(response.getBody().isEmpty());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemById(){
        Item item = TestUtils.createTestItem1();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(response.getBody(), item);
    }

    @Test
    public void getItemByIdFailedByNotFoundItem(){
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemByName(){
        when(itemRepository.findByName("tolm")).thenReturn(null);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("tolm");

        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }
}
