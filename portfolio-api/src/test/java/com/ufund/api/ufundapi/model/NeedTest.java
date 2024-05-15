package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.model.Need;

/**
 * The unit test suite for the Need class
 * 
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class NeedTest {
    @Test
    public void testCreation() {
        
        int expected_id = 1;
        String expected_name = "need";
        int expected_cost = 300;
        int expected_quantity = 3;
        String expected_type = "need";


        // Invoke
        Need need = new Need(expected_id, expected_name, expected_cost, expected_quantity, expected_type);

        // Analyze
        assertEquals(expected_id,need.getId());
        assertEquals(expected_name,need.getName());
        assertEquals(expected_cost,need.getCost());
        assertEquals(expected_quantity, need.getQuantity());
        assertEquals(expected_type, need.getType());

    }


    @Test
    public void testToString() {
        // Setup
        int id = 1;
        String name = "need";
        int cost = 300;
        int quantity = 3;
        String type = "need";
        String expected_string = String.format(Need.STRING_FORMAT, id, name, cost, quantity, type);
        Need need = new Need(id, name, cost, quantity, type);

        // Invoke
        String actual_string = need.toString();

        // Analyze
        assertEquals(expected_string, actual_string);
    }
    @Test
    public void testGetId() {
        Need test1 = new Need(100, null, 0, 0, null);
        Need test2 = new Need(12903, null, 0, 0, null);
        Need test3 = new Need(-123, null, 0, 0, null);
        Need test4 = new Need(0, null, 0, 0, null);

        assertEquals(100, test1.getId());
        assertEquals(12903, test2.getId());
        assertEquals(-123, test3.getId());
        assertEquals(0, test4.getId());
    }
    
    @Test
    public void testNames(){
        Need test1 = new Need(100, "Jason gao", 0, 0, null);
        Need test2 = new Need(12903, "Jason Smith", 0, 0, null);

        assertEquals("Jason gao", test1.getName());
        assertEquals("Jason Smith", test2.getName());

        test1.setName("Carter");
        test2.setName("Spring");

        assertEquals("Carter", test1.getName());
        assertEquals("Spring", test2.getName());
    }

    @Test
    public void testCostsAndQuantity() {
        Need test1 = new Need(100, "Jason gao", 100, 1, null);
        Need test2 = new Need(12903, "Jason Smith", 20000, 1, null);

        assertEquals(100, test1.getCost());
        assertEquals(20000, test2.getCost());

        assertEquals(1, test1.getQuantity());
        assertEquals(1, test2.getQuantity());

        test1.setCost(0);
        test2.setCost(100);

        test1.setQuantity(100);
        test2.setQuantity(200);

        assertEquals(0, test1.getCost());
        assertEquals(100, test2.getCost());

        assertEquals(100, test1.getQuantity());
        assertEquals(200, test2.getQuantity());
    }

    @Test
    public void testEquals(){
        Need test1 = new Need(100, "Jason gao", 100, 1, null);
        Need test2 = new Need(100, "Jason gao", 200, 1, null);

        assertEquals(false, test1.equals(test2));

        test1.setCost(200);;

        assertEquals(true, test1.equals(test2));

        test2.setCost(1);

        assertEquals(false, test2.equals(test1));

        test1.setCost(1);
        
        assertEquals(true, test2.equals(test1));
    }
    
}