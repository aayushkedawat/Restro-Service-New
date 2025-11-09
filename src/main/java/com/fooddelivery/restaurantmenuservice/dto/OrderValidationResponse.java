package com.fooddelivery.restaurantmenuservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderValidationResponse {
    private boolean isValid;
    private String rejectionReason;
    private String restaurantCity; // Returned for Order Service to check Delivery constraint
    private BigDecimal calculatedItemsTotal;
    private List<ValidatedItem> validatedItems;

    @Data
    @Builder
    public static class ValidatedItem {
        private String itemId;
        private BigDecimal price;
        private int quantity;
    }
}
