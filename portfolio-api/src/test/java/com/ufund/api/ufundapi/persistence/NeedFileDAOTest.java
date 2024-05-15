package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Need File DAO class
 * 
 * @author SWEN Faculty
 */
@Tag("Persistence-tier")
public class NeedFileDAOTest {
    private NeedFileDAO NeedFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;


    @BeforeEach
    public void setUp() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        testNeeds[0] = new Need(99, "towel", 4, 1, "hygiene");
        testNeeds[1] = new Need(100,"canned peaches", 1, 1, "food");
        testNeeds[2] = new Need(101,"canned pears", 1, 1, "food");

        when(mockObjectMapper
            .readValue(new File("needfiledaotest.txt"),Need[].class))
                .thenReturn(testNeeds);
        NeedFileDAO = new NeedFileDAO("needfiledaotest.txt",mockObjectMapper);
    }

    @Test
    public void testCreation() {
        assertNotNull(NeedFileDAO);
    }

    @Test
    public void testGetNeed() {
        assertEquals(3, this.NeedFileDAO.getNeeds().length);
    }

    @Test
    public void testFindNeed() {

    }

    @Test
    public void testCreateNeed() throws IOException {
        Need need = new Need(0,"test" ,1, 1,null);
        this.NeedFileDAO.createNeed(need);
        assertEquals(4, this.NeedFileDAO.getNeeds().length);
    }

    @Test
    public void testUpdateNeed() throws IOException {
        Need need = new Need(2,"test" ,1, 1,null);
        this.NeedFileDAO.createNeed(new Need(100, "nothing", 100, 100, null));
        assertEquals(null, this.NeedFileDAO.updateNeed(need));;

        Need test = new Need(100, "new", 1, 1, null);
        assertEquals(true, test.equals(this.NeedFileDAO.updateNeed(test)));
    }

    @Test
    public void testDeleteNeed() throws IOException{
        this.NeedFileDAO.deleteNeed(90);

        assertEquals(3, this.NeedFileDAO.getNeeds().length);

        this.NeedFileDAO.deleteNeed(100);

        assertEquals(2, this.NeedFileDAO.getNeeds().length);

        this.NeedFileDAO.deleteNeed(101);

        assertEquals(1, this.NeedFileDAO.getNeeds().length);
    }
}
