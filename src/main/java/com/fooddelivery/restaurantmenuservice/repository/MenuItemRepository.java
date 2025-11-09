package com.fooddelivery.restaurantmenuservice.repository;

import com.fooddelivery.restaurantmenuservice.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    List<MenuItem> findByRestaurantId(String restaurantId);
    List<MenuItem> findByIdInAndRestaurantId(List<String> itemIds, String restaurantId);
}
