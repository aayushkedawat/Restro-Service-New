package com.fooddelivery.restaurantmenuservice.dto;

// Prices and totals use Double in models (Double instead of BigDecimal)
import java.util.List;
import java.util.Objects;

public class OrderValidationResponse {
    private boolean isValid;
    private String rejectionReason;
    private String restaurantCity; // Returned for Order Service to check Delivery constraint
    private Double calculatedItemsTotal;
    private List<ValidatedItem> validatedItems;

    public OrderValidationResponse() {
    }

    public OrderValidationResponse(boolean isValid, String rejectionReason, String restaurantCity, 
                                 Double calculatedItemsTotal, List<ValidatedItem> validatedItems) {
        this.isValid = isValid;
        this.rejectionReason = rejectionReason;
        this.restaurantCity = restaurantCity;
        this.calculatedItemsTotal = calculatedItemsTotal;
        this.validatedItems = validatedItems;
    }

    public static OrderValidationResponseBuilder builder() {
        return new OrderValidationResponseBuilder();
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRestaurantCity() {
        return restaurantCity;
    }

    public void setRestaurantCity(String restaurantCity) {
        this.restaurantCity = restaurantCity;
    }

    public Double getCalculatedItemsTotal() {
        return calculatedItemsTotal;
    }

    public void setCalculatedItemsTotal(Double calculatedItemsTotal) {
        this.calculatedItemsTotal = calculatedItemsTotal;
    }

    public List<ValidatedItem> getValidatedItems() {
        return validatedItems;
    }

    public void setValidatedItems(List<ValidatedItem> validatedItems) {
        this.validatedItems = validatedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderValidationResponse that = (OrderValidationResponse) o;
        return isValid == that.isValid &&
                Objects.equals(rejectionReason, that.rejectionReason) &&
                Objects.equals(restaurantCity, that.restaurantCity) &&
                Objects.equals(calculatedItemsTotal, that.calculatedItemsTotal) &&
                Objects.equals(validatedItems, that.validatedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isValid, rejectionReason, restaurantCity, calculatedItemsTotal, validatedItems);
    }

    public static class OrderValidationResponseBuilder {
        private boolean isValid;
        private String rejectionReason;
        private String restaurantCity;
    private Double calculatedItemsTotal;
        private List<ValidatedItem> validatedItems;

        OrderValidationResponseBuilder() {
        }

        public OrderValidationResponseBuilder isValid(boolean isValid) {
            this.isValid = isValid;
            return this;
        }

        public OrderValidationResponseBuilder rejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public OrderValidationResponseBuilder restaurantCity(String restaurantCity) {
            this.restaurantCity = restaurantCity;
            return this;
        }

        public OrderValidationResponseBuilder calculatedItemsTotal(Double calculatedItemsTotal) {
            this.calculatedItemsTotal = calculatedItemsTotal;
            return this;
        }

        public OrderValidationResponseBuilder validatedItems(List<ValidatedItem> validatedItems) {
            this.validatedItems = validatedItems;
            return this;
        }

        public OrderValidationResponse build() {
            return new OrderValidationResponse(isValid, rejectionReason, restaurantCity, calculatedItemsTotal, validatedItems);
        }
    }

    public static class ValidatedItem {
        private String itemId;
    private Double price;
        private int quantity;

        public ValidatedItem() {
        }

        public ValidatedItem(String itemId, Double price, int quantity) {
            this.itemId = itemId;
            this.price = price;
            this.quantity = quantity;
        }

        public static ValidatedItemBuilder builder() {
            return new ValidatedItemBuilder();
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValidatedItem that = (ValidatedItem) o;
            return quantity == that.quantity &&
                    Objects.equals(itemId, that.itemId) &&
                    Objects.equals(price, that.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemId, price, quantity);
        }

        public static class ValidatedItemBuilder {
            private String itemId;
            private Double price;
            private int quantity;

            ValidatedItemBuilder() {
            }

            public ValidatedItemBuilder itemId(String itemId) {
                this.itemId = itemId;
                return this;
            }

            public ValidatedItemBuilder price(Double price) {
                this.price = price;
                return this;
            }

            public ValidatedItemBuilder quantity(int quantity) {
                this.quantity = quantity;
                return this;
            }

            public ValidatedItem build() {
                return new ValidatedItem(itemId, price, quantity);
            }
        }
    }
}
