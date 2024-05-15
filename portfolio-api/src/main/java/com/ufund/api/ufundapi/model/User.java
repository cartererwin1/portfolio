package com.ufund.api.ufundapi.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import com.ufund.api.ufundapi.controller.NeedController;

public class User {
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    static final String STRING_FORMAT = "User [id=%d, username=%s, password=%s, baseket=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    //The users basket will just be add the need each time it is clicked on, so if they want to purchase 2 of the same need then they will
    //have 2 identical needs in the same basket (the needs will both have quantities > 1) but it will be ignored during checkout
    @JsonProperty("basket") private ArrayList<Need> basket;
    @JsonProperty("history") private ArrayList<Need> history;
    @JsonProperty("total") private int total;

    public User(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("password")String password){
        this.id = id;
        this.basket = new ArrayList<Need>();
        this.history = new ArrayList<Need>();
        this.username = username;
        this.password = password;
        this.total = 0;
    }


    public void addNeed(Need need){
        this.basket.add(need);
        total += need.getCost();

        //display added need
    }
    public int checkout() {
        for(int i = 0; i < basket.size(); i++){
            history.add(basket.get(i));
        
        } 
        this.basket.clear();
        resetTotal();
        return total;
    }


    public void resetTotal(){
        total = 0;
    }
    public int getTotal(){
        return total;
    }
    public Need[] getHistory(){
        return this.history.toArray(new Need[this.history.size()]);
    }

    public void removeNeed(int id) {
        Iterator<Need> iterator = this.basket.iterator();
        while (iterator.hasNext()) {
            Need need = iterator.next();
            if (need.getId() == id) {
                total -= need.getCost();
                iterator.remove();
                break;
            }
        }
    }

    public String getUsername() {
        return this.username;
    }
    public void setName(String name){
        this.username = name;
    }

    public String getPassword() {
        return this.password;
    }
    public Need[] getBasket() {
        return this.basket.toArray(new Need[this.basket.size()]);
    }
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object other) {
        User o = (User) other;
        if (o.getUsername().equals(this.getUsername())) {
            return true;
        }
        return false;
    }
}
