package com.fooddelivery.restaurantmenuservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    private String name;
    private String cuisine;
    private String city;
    private double rating;
    private boolean isOpen; // CRITICAL: Business rule check
    private LocalDateTime createdAt;

    public Restaurant() {
        this.createdAt = LocalDateTime.now();
    }

    public Restaurant(String id, String name, String cuisine, String city, double rating, boolean isOpen, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.city = city;
        this.rating = rating;
        this.isOpen = isOpen;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public static RestaurantBuilder builder() {
        return new RestaurantBuilder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Double.compare(that.rating, rating) == 0 &&
                isOpen == that.isOpen &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(cuisine, that.cuisine) &&
                Objects.equals(city, that.city) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cuisine, city, rating, isOpen, createdAt);
    }

    public static class RestaurantBuilder {
        private String id;
        private String name;
        private String cuisine;
        private String city;
        private double rating;
        private boolean isOpen;
        private LocalDateTime createdAt = LocalDateTime.now();

        RestaurantBuilder() {
        }

        public RestaurantBuilder id(String id) {
            this.id = id;
            return this;
        }

        public RestaurantBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RestaurantBuilder cuisine(String cuisine) {
            this.cuisine = cuisine;
            return this;
        }

        public RestaurantBuilder city(String city) {
            this.city = city;
            return this;
        }

        public RestaurantBuilder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public RestaurantBuilder isOpen(boolean isOpen) {
            this.isOpen = isOpen;
            return this;
        }

        public RestaurantBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(id, name, cuisine, city, rating, isOpen, createdAt);
        }
    }
}
