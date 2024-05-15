package com.ufund.api.ufundapi.model;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;


@Tag("Model-tier")
public class UFundTest {
    @Test
    public void testCreation() {
        //Setup
        HashMap<String, Need> expected_needsCupboard = new HashMap<>();
        ArrayList<User> expected_users = new ArrayList<>();

        // Invoke
        UFund ufund = new UFund();

        // Analyze
        assertEquals(expected_needsCupboard, ufund.getNeedsCupboard());
        assertEquals(expected_users, ufund.getUsers());
    }


    @Test
    public void testAddUser() {
        // Setup
        ArrayList<User> expected_users = new ArrayList<>();
        User user1 = new User(1, "user1", "one");
        expected_users.add(user1);
        //Invoke
        UFund ufund = new UFund();
        ufund.addUser(user1);

        //Analyze
        assertEquals(expected_users, ufund.getUsers());
    }


    @Test
    public void testAddNeed() {
        // Setup
        HashMap<String, Need> expected_needsCupboard = new HashMap<String, Need>();
        Need need1 = new Need(1, "need1", 1, 1, "need");
        expected_needsCupboard.put("need1", need1);
        //Invoke
        UFund ufund = new UFund();
        ufund.addNeed(need1);

        //Analyze
        assertEquals(expected_needsCupboard, ufund.getNeedsCupboard());
    }


    @Test
    public void testRemoveNeed() {
        // Setup
        HashMap<String, Need> expected_needsCupboard = new HashMap<String, Need>();
        Need need1 = new Need(1, "need1", 1, 1, "need");
        //Invoke
        UFund ufund = new UFund();
        ufund.addNeed(need1);
        ufund.removeNeed(need1);

        //Analyze
        assertEquals(expected_needsCupboard, ufund.getNeedsCupboard());
    }

    @Test
    public void testIsNewUser() {
        // Setup
        User user1 = new User(1, "user1", "one");
        User user2 = new User(2, "user2", "two");

        //Invoke
        UFund ufund = new UFund();
        ufund.addUser(user1);

        //Analyze
        assertFalse(ufund.isNewUser(user1));
        assertTrue(ufund.isNewUser(user2));

    }

    @Test
    public void testIsAdmin() {
        // Setup
        User admin = new User(1, "Yuh", "Team 3");
        User user1 = new User(2, "user1", "one");

        //Invoke
        UFund ufund = new UFund();

        //Analyze
        assertTrue(ufund.isAdmin(admin));
        assertFalse(ufund.isAdmin(user1));

    }

    @Test
    public void testCheckout() {
        // Setup
        User user1 = new User(1, "user1", "one");
        Need need = new Need(0, "need", 0, 1, "need");
        //Need need2 = new Need(0, "need", 0, 1, "need");
        //expected_needsCupboard.put(need2.getName(), need2);
        //Invoke
        UFund ufund = new UFund();
        ufund.addNeed(need);
        user1.addNeed(need);
        ufund.checkout(user1);
        //Analyze
        assertEquals(0,user1.getBasket().length);
        assertEquals(0, ufund.getNeedsCupboard().keySet().size());
    }
}
