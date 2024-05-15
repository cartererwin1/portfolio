package com.ufund.api.ufundapi.model;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UFund {
    @JsonProperty("adminPassword") private final String adminPassword = "Team 3";
    @JsonProperty("adminUsername") private final String adminUsername = "Yuh";
    @JsonProperty("users") private ArrayList<User> users;
    @JsonProperty("needsCupboard") private HashMap<String, Need> needsCupboard;

    public UFund() {
        this.users = new ArrayList<User>();
        this.needsCupboard = new HashMap<String, Need>();
    }

    public void addNeed(Need need) {
        this.needsCupboard.put(need.getName(), need);
    }

    //The need param will be passed when a user clicks on the need to remove that button needs to return the need that was clicked on
    public void removeNeed(Need need){
        //if the quantity of the need is only 1
        if(this.needsCupboard.get(need.getName()).getQuantity() == 1) {
            needsCupboard.remove(need.getName());
        } else {
            //otherwise the quantity is more than one therefore the quantity will be decreased by one
            Need tempneed = this.needsCupboard.get(need.getName());
            tempneed.setQuantity(this.needsCupboard.get(need.getName()).getQuantity() - 1);
            needsCupboard.remove(need.getName());
            needsCupboard.put(tempneed.getName(), tempneed);
        }
        //display confirmation
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public boolean isNewUser(User user){
        if(users.contains(user)){
            return false;
            //if False tell user account does not exist
            //go back to login page, have a button at the bottom to create account
        }
        else{
            return true;
            //If return true, go to dashboard        
        }
    }
    public boolean isAdmin(User user){
        if(user.getUsername().equals(adminUsername) && user.getPassword().equals(adminPassword)){
            return true;
        }
        else{
            return false;
            //if false, they are not an admin, send to authenticate
        }
    }

    public int checkout(User user) {
        int total = 0;
        for(Need need : user.getBasket()) {
            total += need.getCost();
            this.removeNeed(need);
        }
        user.checkout();
        return total;
    }

    public HashMap<String, Need> getNeedsCupboard() {
        return this.needsCupboard;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }
}
