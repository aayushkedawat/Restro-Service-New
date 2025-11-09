package com.fooddelivery.restaurantmenuservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field; // Ensure this is imported
import java.time.LocalDateTime;

@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    private String name;
    private String cuisine;
    private String city;
    private Double rating;      
    
    // Use the exact field name from the database documents for clarity
    @Field("isOpen")
    private boolean isOpen;     
    
    private LocalDateTime createdAt;

    // Required No-Argument Constructor
    public Restaurant() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters (Ensuring unambiguous mapping) ---
    
    // Standard Getters/Setters for basic types
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Boolean accessors: provide both JavaBean-compatible names and convenience methods
    public boolean isOpen() { return isOpen; }
    public boolean getIsOpen() { return isOpen; }
    public void setIsOpen(boolean isOpen) { this.isOpen = isOpen; }
    // Backwards-compatible setters
    public void setOpen(boolean open) { this.isOpen = open; }
}