package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.UFund;
import com.ufund.api.ufundapi.model.User;



/**
 * Implements the functionality for JSON file-based peristance for Users
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as usered
 * 
 * @author SWEN Faculty
 */
@Component
public class UserFileDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());

    Map<String, User> users;
    //UFund ufund;

    private ObjectMapper objectMapper;
    private static int nextId;
    private String filename;

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the users from the file
    }

    /**
     * Generates the next id for a new {@linkplain User user}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain User users} from the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Generates an array of {@linkplain User Users} from the tree map for any
     * {@linkplain User Users} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain User Users}
     * in the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUsersArray(String containsText) { // if containsText == null, no filter
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            if (containsText == null || user.getUsername().contains(containsText)) {
                userArrayList.add(user);
            }
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Saves the {@linkplain User users} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User users} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] userArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    /**
     * Loads {@linkplain User users} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of users
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] userArray = objectMapper.readValue(new File(filename),User[].class);
        // Add each user to the tree map and keep track of the greatest id
        for (User user: userArray) {
            users.put(user.getUsername(),user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

   public boolean isNewUser(String username) {
        if(users.containsKey(username)) {
            return false;
        } else {
            return true;
        }
    }

    public User login(String username, String password) {
        if(isNewUser(username)) {
            User newUser = new User(nextId, username, password);
            users.put(username, newUser);
            return newUser;
        } else {
            return users.get(username);
        }
    }


    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] getUsers() {
        synchronized(users) {
            return getUsersArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] findUsers(String containsText) {
        synchronized(users) {
            return getUsersArray(containsText);
        }
    }

    
    @Override
    public User getUser(String username) {
        synchronized(users) {
            if (users.containsKey(username))
                return users.get(username);
            else
                return null;
        }
    }

    @Override
    public int getUserID(String username) {
        synchronized(users) {
            if (users.containsKey(username))
                return users.get(username).getId();
            else
                return -1;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User updateUser(User user) throws IOException {
        synchronized(users) {
            if (users.containsKey(user.getId()) == false)
                return null;  // user does not exist

            users.put(user.getUsername(),user);
            save(); // may throw an IOException
            return user;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteUser(String username) throws IOException {
        synchronized(users) {
            if (users.containsKey(username)) {
                users.remove(username);
                return save();
            }
            else
                return false;
        }
    }
    /**
    ** {@inheritDoc}
     */
    @Override
    public User createUser(User user) throws IOException {
        synchronized(users) {
            // We create a new user object because the id field is immutable
            // and we user to assign the next unique id
            User newUser = new User(nextId(),user.getUsername(), user.getPassword());
            users.put(newUser.getUsername(),newUser);
            save(); // may throw an IOException
            return newUser;
        }
    }
  

}
