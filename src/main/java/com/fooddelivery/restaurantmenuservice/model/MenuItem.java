package com.fooddelivery.restaurantmenuservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Data
@Document(collection = "menu_items")
public class MenuItem {
    @Id
    private String id;
    private String restaurantId; // Foreign Key reference to Restaurant
    private String name;
    private String category;
    private BigDecimal price; // CRITICAL: Pricing source of truth
    private boolean isAvailable; // CRITICAL: Item availability check
}
