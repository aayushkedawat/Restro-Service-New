package com.fooddelivery.restaurantmenuservice.service;

import com.fooddelivery.restaurantmenuservice.dto.OrderValidationRequest;
import com.fooddelivery.restaurantmenuservice.dto.OrderValidationResponse;
import com.fooddelivery.restaurantmenuservice.model.MenuItem;
import com.fooddelivery.restaurantmenuservice.model.Restaurant;
import com.fooddelivery.restaurantmenuservice.repository.MenuItemRepository;
import com.fooddelivery.restaurantmenuservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantMenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

        /**
         * CRITICAL API: Validates order against business rules (is_open, is_available, pricing).
         * This is called synchronously by the Order Service to achieve decoupling (API Composition).
         */
    public OrderValidationResponse validateOrder(OrderValidationRequest request) {
        // 1. Check Restaurant Existence and Status
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found."));

        // Business Rule: A restaurant may accept orders only when is_open=true.
        if (!restaurant.isOpen()) {
            return OrderValidationResponse.builder()
                    .isValid(false)
                    .rejectionReason("Reject orders if restaurant is closed. Restaurant is currently closed and cannot accept orders.")
                    .build();
        }

        // 2. Fetch all requested Menu Items
        List<String> requestedItemIds = request.getItems().stream()
                .map(OrderValidationRequest.ItemRequest::getItemId)
                .toList();

        List<MenuItem> actualMenuItems = menuItemRepository
                .findByIdInAndRestaurantId(requestedItemIds, request.getRestaurantId());

        if (actualMenuItems.size() != requestedItemIds.size()) {
            return OrderValidationResponse.builder()
                    .isValid(false)
                    .rejectionReason("One or more requested items do not exist on the menu.")
                    .build();
        }

        Map<String, MenuItem> itemMap = actualMenuItems.stream()
                .collect(Collectors.toMap(MenuItem::getId, item -> item));

        // 3. Final Availability and Total Calculation
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        List<OrderValidationResponse.ValidatedItem> validatedItems = new java.util.ArrayList<>();

        for (OrderValidationRequest.ItemRequest requestedItem : request.getItems()) {
            MenuItem currentItem = itemMap.get(requestedItem.getItemId());

                        // Business Rule: All requested items must be is_available=true.
            if (!currentItem.isAvailable()) {
                return OrderValidationResponse.builder()
                        .isValid(false)
                                                .rejectionReason("Reject orders if item not available. Item '" + currentItem.getName() + "' is currently unavailable.")
                        .build();
            }

            // Calculate item subtotal and cumulative total
            BigDecimal itemSubtotal = currentItem.getPrice().multiply(BigDecimal.valueOf(requestedItem.getQuantity()));
            calculatedTotal = calculatedTotal.add(itemSubtotal);

            validatedItems.add(OrderValidationResponse.ValidatedItem.builder()
                    .itemId(currentItem.getId())
                    .price(currentItem.getPrice()) // Send back the official price
                    .quantity(requestedItem.getQuantity())
                    .build());
        }

        // 4. Success Response
        return OrderValidationResponse.builder()
                .isValid(true)
                .restaurantCity(restaurant.getCity()) // CRITICAL for Delivery Service check
                .calculatedItemsTotal(calculatedTotal) // Used by Order Service to calculate final total
                .validatedItems(validatedItems)
                .build();
    }
}
