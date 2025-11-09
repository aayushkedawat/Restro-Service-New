package com.fooddelivery.restaurantmenuservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderValidationRequest {
    private String restaurantId;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private String itemId;
        private int quantity;
    }
}
