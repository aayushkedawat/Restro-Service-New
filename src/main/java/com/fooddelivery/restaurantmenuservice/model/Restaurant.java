package com.fooddelivery.restaurantmenuservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    private String name;
    private String cuisine;
    private String city;
    private double rating;
    private boolean isOpen; // CRITICAL: Business rule check
    private LocalDateTime createdAt = LocalDateTime.now();
}
