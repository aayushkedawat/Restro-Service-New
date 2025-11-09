package com.fooddelivery.restaurantmenuservice.dto;

import java.util.List;

public class OrderValidationRequest {
    private String restaurantId;
    private List<ItemRequest> items;

    public OrderValidationRequest() {
    }

    public OrderValidationRequest(String restaurantId, List<ItemRequest> items) {
        this.restaurantId = restaurantId;
        this.items = items;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }

    public static class ItemRequest {
        private String itemId;
        private int quantity;

        public ItemRequest() {
        }

        public ItemRequest(String itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
