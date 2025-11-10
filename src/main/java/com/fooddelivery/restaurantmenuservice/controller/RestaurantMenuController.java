package com.fooddelivery.restaurantmenuservice.controller;

import com.fooddelivery.restaurantmenuservice.dto.OrderValidationRequest;
import com.fooddelivery.restaurantmenuservice.dto.OrderValidationResponse;
import com.fooddelivery.restaurantmenuservice.model.Restaurant;
import com.fooddelivery.restaurantmenuservice.service.RestaurantMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/v1/restaurants")
@Tag(name = "Restaurant Menu", description = "APIs for managing restaurants and menu items")
public class RestaurantMenuController {

    private final RestaurantMenuService service;

    public RestaurantMenuController(RestaurantMenuService service) {
        this.service = service;
    }

    // ----------------------------------------------------
    // CRITICAL API for Inter-Service Communication (Order Service)
    // ----------------------------------------------------

    @Operation(summary = "Validate Order", description = "Critical API for Order Service to validate restaurant status, item availability, and calculate pricing")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order is valid"),
        @ApiResponse(responseCode = "409", description = "Order validation failed - restaurant closed or items unavailable"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PostMapping("/validate-order")
    public ResponseEntity<OrderValidationResponse> validateOrder(@RequestBody OrderValidationRequest request) {
        OrderValidationResponse response = service.validateOrder(request);

        if (!response.isValid()) {
            // Return 409 Conflict (Client Error) if business rule violated, indicating rejection
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Return 200 OK with pricing and city data
        return ResponseEntity.ok(response);
    }

    // ----------------------------------------------------
    // Public/Admin Endpoints (CRUD)
    // ----------------------------------------------------

    @Operation(summary = "Get All Restaurants", description = "List all restaurants with optional city filter and pagination support")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurants")
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants(
        @Parameter(description = "Filter by city name") @RequestParam(required = false) String city,
        Pageable pageable) {
        return ResponseEntity.ok(service.getAllRestaurants(city, pageable));
    }

    @Operation(summary = "Get Restaurant by ID", description = "Retrieve detailed information about a specific restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant found"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@Parameter(description = "Restaurant ID") @PathVariable String id) {
        return ResponseEntity.ok(service.getRestaurantById(id));
    }

    @Operation(summary = "Get Restaurant Menu", description = "Retrieve the complete menu for a specific restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}/menu")
    public ResponseEntity<List<com.fooddelivery.restaurantmenuservice.model.MenuItem>> getRestaurantMenu(@Parameter(description = "Restaurant ID") @PathVariable String id) {
        return ResponseEntity.ok(service.getRestaurantMenu(id));
    }

    @Operation(summary = "Create Restaurant", description = "Admin endpoint to create a new restaurant")
    @ApiResponse(responseCode = "201", description = "Restaurant created successfully")
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRestaurant(restaurant));
    }

    @Operation(summary = "Update Menu Item", description = "Admin endpoint to update menu item price or availability")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
        @ApiResponse(responseCode = "404", description = "Menu item not found")
    })
    @PutMapping("/menu-items/{id}")
    public ResponseEntity<com.fooddelivery.restaurantmenuservice.model.MenuItem> updateMenuItem(
        @Parameter(description = "Menu Item ID") @PathVariable String id,
        @RequestBody com.fooddelivery.restaurantmenuservice.model.MenuItem menuItem) {
        return ResponseEntity.ok(service.updateMenuItem(id, menuItem));
    }
}
