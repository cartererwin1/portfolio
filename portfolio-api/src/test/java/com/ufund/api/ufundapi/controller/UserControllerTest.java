package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.imageio.IIOException;

import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Tag("Controller-tier")
public class UserControllerTest {
    private UserController UserController;
    private UserDAO mockUserDAO;

    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        UserController = new UserController(mockUserDAO);
    }

    @Test
    public void testGetuser() throws IOException{
        User user = new User(100, "M", "MMM");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        ResponseEntity<User> response = UserController.getUser(user.getUsername());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

    }

     @Test
    public void testGetUserNotFound() throws Exception { 
        String username = "MMM";
        when(mockUserDAO.getUser(username)).thenReturn(null);
        ResponseEntity<User> response = UserController.getUser(username);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception { 
        String username = "MMM";
        doThrow(new IOException()).when(mockUserDAO).getUser(username);
        ResponseEntity<User> response = UserController.getUser(username);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUserBasket() throws IOException{
        User user = new User(100, "M", "MMM");
        ResponseEntity<Need[]> response = UserController.getUserBasket(user.getUsername());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void testGetUserBasketNotFound() throws IOException{
        String username = "123";
        ResponseEntity<Need[]> response = UserController.getUserBasket(username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


    }

    @Test
    public void testGetUsers() throws IOException{
        User[] users = new User[2];
        users[0] = new User(111, "123", "123");
        users[1] = new User(222, "12332", "142");
        when(mockUserDAO.getUsers()).thenReturn(users);
        ResponseEntity<User[]> response = UserController.getUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test 
    public void testGetUsersHandleException() throws IOException{
        doThrow(new IOException()).when(mockUserDAO).getUsers();
        ResponseEntity<User[]> response = UserController.getUsers();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchUsers() throws IOException{
        String search = "ma";
        User[] users = new User[2];
        users[0] = new User(111, "MMM", "MMM");
        users[1] = new User(222, "MMMMMM", "MMMMMM");
        ResponseEntity<User[]> response = UserController.searchUsers(search);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException{
        User user = new User(111, "MMM", "MMMMM");
        when(mockUserDAO.createUser(user)).thenReturn(user);
        ResponseEntity<User> response = UserController.createUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test 
    public void testCreateUserFailed() throws IOException{
        User user = new User(111, "MMM", "MMMM");
        when(mockUserDAO.createUser(user)).thenReturn(null);
        ResponseEntity<User> response = UserController.createUser(user);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException {  
        User user = new User(111, "soup 2", "can");
        doThrow(new IOException()).when(mockUserDAO).createUser(user);
        ResponseEntity<User> response = UserController.createUser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testAddToBasket() throws IOException{
        User user = new User(111, "soup", "moresoup");
        Need need = new Need(111, "MMM", 0, 0, "food");
        ResponseEntity<User> response = UserController.addToBasket(user.getUsername(), need);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void testAddToBasketFailed() throws IOException{
        Need need = new Need(111, "MMM", 0, 0, "food");
        ResponseEntity<User> response = UserController.addToBasket(null, need);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
    }

    @Test
    public void testRemoveSingleBasket() throws IOException{
        User user = new User(111, "MMM", "MMMM");
        Need need = new Need(222, "soup", 0, 0, "Food");
        UserController.addToBasket(user.getUsername(), need);
        ResponseEntity<User> response = UserController.removeSingleBasket(user.getUsername(), need.getId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void testRemoveSingleBasketFailed() throws IOException{
        Need need = new Need(222, "soup", 0, 0, "Food");
        ResponseEntity<User> response = UserController.removeSingleBasket(null, need.getId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }

    @Test
    public void testCheckout() throws IOException{
        User user = new User(111, "MMM", "MMMM");
        Need need = new Need(222, "soup", 5, 0, "Food");
        int total = 5;
        UserController.addToBasket(user.getUsername(), need);
        ResponseEntity<Integer> response = UserController.checkout(user.getUsername());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());

    }
    @Test
    public void testCheckoutFailed() throws IOException{
        Need need = new Need(222, "soup", 0, 0, "Food");
        ResponseEntity<Integer> response = UserController.checkout(null);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }

    @Test
    public void testLogin() throws IOException{
        User user = new User(111, "12312", "12423");
        when(mockUserDAO.login(user.getUsername(), user.getPassword())).thenReturn(user);
        ResponseEntity<User> response = UserController.login(user.getUsername(), user.getPassword());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

    }

    @Test
    public void testLoginFailed(){
        User user = new User(111, "", "");
        when(mockUserDAO.login(user.getUsername(), user.getPassword())).thenReturn(null);
        ResponseEntity<User> repsonse = UserController.login(user.getUsername(), user.getPassword());
        assertEquals(HttpStatus.NOT_FOUND, repsonse.getStatusCode());

    }

    @Test 
    public void testUpdateUser() throws IOException{
        User user = new User(111, "MMM", "MMMMM");
        when(mockUserDAO.updateUser(user)).thenReturn(user);
        ResponseEntity<User> response = UserController.updateuser(user);
        user.setName("MMMMM");
        response = UserController.updateuser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

    }

    @Test
    public void testUpdateUserFailed() throws IOException{
        User user = new User(111, "soup 4", "can");
        when(mockUserDAO.updateUser(user)).thenReturn(null);
        ResponseEntity<User> response = UserController.updateuser(user);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws IOException{
        User user = new User(111, "soup 4", "can");
        doThrow(new IOException()).when(mockUserDAO).updateUser(user);
        ResponseEntity<User> response = UserController.updateuser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException{
        String user = "MMMM";
        when(mockUserDAO.deleteUser(user)).thenReturn(true);
        ResponseEntity<User> response = UserController.deleteUser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserFailed() throws IOException{
        String user = "MMM";
        when(mockUserDAO.deleteUser(user)).thenReturn(false);
        ResponseEntity<User> response = UserController.deleteUser(user);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


 @Test
    public void testDeleteUserHandleException() throws IOException{
        String user = "MMM";
        doThrow(new IOException()).when(mockUserDAO).deleteUser(user);
        ResponseEntity<User> response = UserController.deleteUser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }














  




    
}
