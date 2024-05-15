package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCreation() {
        
        String expected_username = "username";
        String expected_password = "password";


        // Invoke
        User user = new User(1, expected_username, expected_password);
        // Analyze
        assertEquals(expected_username,user.getUsername());
        assertEquals(expected_password,user.getPassword());
        assertEquals(0, user.getBasket().length);
    }


    @Test
    public void testAddNeed() {
        // Setup
        Need need = new Need(1, "need", 1, 1, "need");
        Need[] expected_basket = new Need[] {need};
        // Invoke
        User user = new User(1, "username", "password");
        user.addNeed(need);
        // Analyze
        assertEquals(expected_basket[0], user.getBasket()[0]);
    }

    @Test
    public void testRemoveNeed() {
        // Setup
        Need need = new Need(1, "need", 1, 1, "need");
        // Invoke
        User user = new User(1, "username", "password");
        user.addNeed(need);
        user.removeNeed(need.getId());
        // Analyze
        assertEquals(0, user.getBasket().length);
    }

    @Test
    public void testSetName(){
        User user = new User(111, "MMM", "MMMMM");
        user.setName("Soup");
        String expected = "Soup";
        assertEquals(expected, user.getUsername());
    }

    @Test
    public void testId(){
        User test1 = new User(100, "jason", "no");
        User test2 = new User(1000, "dick", "grayson");

        assertEquals(100, test1.getId());
        assertEquals(1000, test2.getId());
    }
    
    @Test
    public void testUsernameAndPassword() {
        User test1 = new User(100, "jason", "no");
        User test2 = new User(1000, "dick", "grayson");

        assertEquals("jason", test1.getUsername());
        assertEquals("dick", test2.getUsername());

        test1.setName("joseph");
        test2.setName("nightwing");

        assertEquals("joseph", test1.getUsername());
        assertEquals("nightwing", test2.getUsername());

        assertEquals("no", test1.getPassword());
        assertEquals("grayson", test2.getPassword());

    }

    @Test
    public void testCheckout() {
        User test1 = new User(100, "jason", "no");
        test1.addNeed(new Need(100, "whatever", 100, 1, null));
        test1.addNeed(new Need(1, "w", 2, 3, null));
        test1.checkout();
        assertEquals(0, test1.getBasket().length);
    }

    @Test
    public void testEquals() {
        User test1 = new User(100, "jason", "grayson");
        User test2 = new User(1000, "dick", "grayson");

        assertEquals(false, test1.equals(test2));
        assertEquals(false, test2.equals(test1));

        test1.setName("dick");
        
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
    }
}