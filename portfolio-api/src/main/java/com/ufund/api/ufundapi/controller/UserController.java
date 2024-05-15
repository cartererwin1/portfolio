package com.ufund.api.ufundapi.controller;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.RepaintManager;

import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.persistence.UserDAO;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.model.UFund;
import com.ufund.api.ufundapi.model.Need;


/**
 * Handles the REST API requests for the User resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author 
 */
@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO UserDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param userDao The {@link UserDAO User Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO UserDao) {
        this.UserDao = UserDao;
    }

    /**
     * Responds to the GET request for a {@linkplain User user} for the given id
     * 
     * @param id The id used to locate the {@link User user}
     * 
     * @return ResponseEntity with {@link User user} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        LOG.info("GET /users/" + username);
        try {
            User user = UserDao.getUser(username);
            if (user != null)
                return new ResponseEntity<User>(user,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{username}/basket")
    public ResponseEntity<Need[]> getUserBasket(@PathVariable String username) {
        LOG.info("GET /users/" + username);
        try {
            User user = UserDao.getUser(username);
            if (user != null)
                return new ResponseEntity<Need[]>(user.getBasket(),HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/isNewUser")
    public ResponseEntity<Boolean> isNewUser(@RequestParam String username) {
        LOG.info("GET/ isNewUser");
        boolean isNew = UserDao.isNewUser(username);
        return new ResponseEntity<Boolean>(isNew, HttpStatus.OK);
    }
    

    /**
     * Responds to the GET request for all {@linkplain User users}
     * 
     * @return ResponseEntity with array of {@link User user} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");

        // Replace below with your implementation
        try {
            User[] users = UserDao.getUsers();
            return new ResponseEntity<User[]>(users,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User users} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link User users}
     * 
     * @return ResponseEntity with array of {@link User user} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all users that contain the text "ma"
     * GET http://localhost:8080/users/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<User[]> searchUsers(@RequestParam String name) {
        LOG.info("GET /users/?name="+name);
        try {
            User[] user = UserDao.findUsers(name);
            return new ResponseEntity<User[]>(user,HttpStatus.OK);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain User user} with the provided user object
     * 
     * @param user - The {@link User user} to create
     * 
     * @return ResponseEntity with created {@link User user} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link User user} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /users " + user);
        try {
            User newUser = UserDao.createUser(user);
            if(newUser != null)
                return new ResponseEntity<User>(newUser,HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{username}/basketadd")
    public ResponseEntity<User> addToBasket(@PathVariable String username, @RequestBody Need need) {
        LOG.info("POST /users/" + username + "/basket");
        try {
            User user = UserDao.getUser(username);
            if(user != null) {
                if(need.getQuantity()-1 < 0){
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                user.addNeed(need);
                return new ResponseEntity<User>(user,HttpStatus.CREATED);
            }
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{username}/basketremoveone/{id}")
    public ResponseEntity<User> removeSingleBasket(@PathVariable String username, @PathVariable int id) {
        LOG.info("DELETE /users/" + username + "/basketremoveone/" + id);
        try {
            User user = UserDao.getUser(username);
            if(user != null) {
                user.removeNeed(id);
                return new ResponseEntity<User>(user,HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{username}/checkout")
    public ResponseEntity<Integer> checkout(@PathVariable String username) {
        LOG.info("DELETE /users/" + username + "/checkout");
        try {
            User user = UserDao.getUser(username);
            if(user != null) {
                user.checkout();
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody String username, @RequestBody String password){
       
        if(username.length() > 0 && password.length() > 0) {
            User user = UserDao.login(username, password);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }                
    }

    
    
    /**
     * Updates the {@linkplain User user} with the provided {@linkplain User user} object, if it exists
     * 
     * @param user The {@link User user} to update
     * 
     * @return ResponseEntity with updated {@link User user} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<User> updateuser(@RequestBody User user) {
        LOG.info("PUT /users " + user);
        try {
            User newUser = UserDao.updateUser(user);
            if(newUser != null) 
                return new ResponseEntity<User>(newUser,HttpStatus.OK);
            else
                return new ResponseEntity<>(newUser,HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain User user} with the given id
     * 
     * @param id The id of the {@link User user} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        LOG.info("DELETE /users/" + username);
        try {
            boolean deleted = UserDao.deleteUser(username);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{username}/history")
    public ResponseEntity<Need[]> getHistory(@PathVariable String username){
        LOG.info("GET /users/" + username);
        try{
            User user = UserDao.getUser(username);
            if(user != null){
                return new ResponseEntity<>(user.getHistory(), HttpStatus.OK);

            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @GetMapping("{username}/total")
    public ResponseEntity<Integer> getTotal(@PathVariable String username){
        LOG.info("GET /users/"+ username+ "/total/");
        try {
            User user = UserDao.getUser(username);
            if(user != null){
                int total = user.getTotal();
                return new ResponseEntity<Integer>(total, HttpStatus.OK);

            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            
        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
    