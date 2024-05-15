package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController NeedController;
    private NeedDAO mockNeedDAO;

  
    @BeforeEach
    public void setupNeedController() {
        mockNeedDAO = mock(NeedDAO.class);
        NeedController = new NeedController(mockNeedDAO);
    }

    @Test
    public void testGetneed() throws IOException {
        Need need = new Need(111, "soup", 200, 1, "can");
        when(mockNeedDAO.getNeed(need.getId())).thenReturn(need);
        ResponseEntity<Need> response = NeedController.getNeed(need.getId());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { 
        int needId = 99;
        when(mockNeedDAO.getNeed(needId)).thenReturn(null);
        ResponseEntity<Need> response = NeedController.getNeed(needId);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { 
        int needId = 99;
        doThrow(new IOException()).when(mockNeedDAO).getNeed(needId);
        ResponseEntity<Need> response = NeedController.getNeed(needId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateNeed() throws IOException {  
        Need need = new Need(111, "soup 0", 200, 1, "can");
        when(mockNeedDAO.createNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = NeedController.createNeed(need);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testCreateNeedFailed() throws IOException { 
        Need need = new Need(111, "soup 1", 200, 1, "can");
        when(mockNeedDAO.createNeed(need)).thenReturn(null);
        ResponseEntity<Need> response = NeedController.createNeed(need);
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException {  
        Need need = new Need(111, "soup 2", 200, 1, "can");
        doThrow(new IOException()).when(mockNeedDAO).createNeed(need);
        ResponseEntity<Need> response = NeedController.createNeed(need);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { 
        Need need = new Need(111, "soup 3", 200, 1, "can");
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = NeedController.updateneed(need);
        need.setName("Bolt");
        response = NeedController.updateneed(need);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { 
        Need need = new Need(111, "soup 4", 200, 1, "can");
        when(mockNeedDAO.updateNeed(need)).thenReturn(null);
        ResponseEntity<Need> response = NeedController.updateneed(need);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException {
        Need need = new Need(111, "soup 5", 200, 1, "can");
        doThrow(new IOException()).when(mockNeedDAO).updateNeed(need);
        ResponseEntity<Need> response = NeedController.updateneed(need);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws IOException { 
        Need[] needs = new Need[2];
        needs[0] = new Need(111, "soup", 200, 1, "can");
        needs[1] = new Need(333, "fake soup", 300, 5, "man");
        when(mockNeedDAO.getNeeds()).thenReturn(needs);
        ResponseEntity<Need[]> response = NeedController.getNeeds();
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { 
        doThrow(new IOException()).when(mockNeedDAO).getNeeds();
        ResponseEntity<Need[]> response = NeedController.getNeeds();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchNeeds() throws IOException { 
        String searchString = "la";
        Need[] needs = new Need[2];
        needs[0] = new Need(111, "soup", 200, 1, "can");
        needs[1] = new Need(333, "fake soup", 300, 5, "man");
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(needs);
        ResponseEntity<Need[]> response = NeedController.searchNeeds(searchString);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testSearchNeedsHandleException() throws IOException { 
        String searchString = "an";
        doThrow(new IOException()).when(mockNeedDAO).findNeeds(searchString);
        ResponseEntity<Need[]> response = NeedController.searchNeeds(searchString);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException { 
        int needId = 99;
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(true);
        ResponseEntity<Need> response = NeedController.deleteNeed(needId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { 
        int needId = 99;
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(false);
        ResponseEntity<Need> response = NeedController.deleteNeed(needId);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException {
        int needId = 99;
        doThrow(new IOException()).when(mockNeedDAO).deleteNeed(needId);
        ResponseEntity<Need> response = NeedController.deleteNeed(needId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
