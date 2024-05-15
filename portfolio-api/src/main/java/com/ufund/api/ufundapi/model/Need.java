package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Need entity
 * 
 * @author 
 */
public class Need {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());
    
    static final String STRING_FORMAT = "Need [id=%d, name=%s]";


    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("cost") private int cost;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("type") private String type;

    /**
     * Create a need with the given id and name
     * @param id The id of the need
     * @param name The name of the need
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") int cost, @JsonProperty("quantity") int quantity, @JsonProperty("type") String type) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
    }

    /**
     * Retrieves the id of the need
     * @return The id of the need
     */
    public int getId() {return id;}

    /**
     * Sets the name of the need - necessary for JSON object to Java object deserialization
     * @param name The name of the need
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the need
     * @return The name of the need
     */
    public String getName() {return name;}


    public void setCost(int cost) {this.cost = cost;}


    public int getCost() {return cost;}


    public void setQuantity(int quantity) {this.quantity = quantity;}


    public int getQuantity() {return quantity;}


    public void setType(String type) {this.type = type;}


    public String getType() {return type;}

    @Override
    public boolean equals(Object other) {
        Need otherNeed = (Need) other;
        return (this.id == otherNeed.getId() && this.name.equals(otherNeed.getName()) && this.cost == otherNeed.getCost() 
        && this.quantity == otherNeed.getQuantity());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,cost, quantity, type);
    }
}
