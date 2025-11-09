package com.fooddelivery.restaurantmenuservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;
// import java.math.BigDecimal; // Deleted - no longer used

@Document(collection = "menu_items")
public class MenuItem {
    @Id
    private String id;
    private String restaurantId; // Foreign Key reference to Restaurant
    private String name;
    private String category;
    private Double price; // CRITICAL: Pricing source of truth - now consistently Double
    private boolean isAvailable; // CRITICAL: Item availability check

    public MenuItem() {}

    // FIXED: Constructor now uses Double for price
    public MenuItem(String id, String restaurantId, String name, String category, Double price, boolean isAvailable) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public static MenuItemBuilder builder() {
        return new MenuItemBuilder();
    }

    // Getters and Setters (using Double)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() { // FIXED: Returns Double
        return price;
    }

    public void setPrice(Double price) { // FIXED: Accepts Double
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return isAvailable == menuItem.isAvailable &&
                Objects.equals(id, menuItem.id) &&
                Objects.equals(restaurantId, menuItem.restaurantId) &&
                Objects.equals(name, menuItem.name) &&
                Objects.equals(category, menuItem.category) &&
                Objects.equals(price, menuItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantId, name, category, price, isAvailable);
    }

    // Builder (using Double)

    public static class MenuItemBuilder {
        private String id;
        private String restaurantId;
        private String name;
        private String category;
        private Double price; // FIXED: Uses Double
        private boolean isAvailable;

        MenuItemBuilder() {
        }

        public MenuItemBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MenuItemBuilder restaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public MenuItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuItemBuilder category(String category) {
            this.category = category;
            return this;
        }

        public MenuItemBuilder price(Double price) { // FIXED: Accepts Double
            this.price = price;
            return this;
        }

        public MenuItemBuilder isAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public MenuItem build() {
            return new MenuItem(id, restaurantId, name, category, price, isAvailable);
        }
    }
}