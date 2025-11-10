package com.fooddelivery.restaurantmenuservice.config;

import com.fooddelivery.restaurantmenuservice.model.MenuItem;
import com.fooddelivery.restaurantmenuservice.model.Restaurant;
import com.fooddelivery.restaurantmenuservice.repository.MenuItemRepository;
import com.fooddelivery.restaurantmenuservice.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataLoader implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public DataLoader(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (restaurantRepository.count() > 0) {
            System.out.println("Data already exists. Skipping initial data load.");
            return;
        }

        System.out.println("Loading initial data from CSV files...");
        loadRestaurants();
        loadMenuItems();
        System.out.println("Initial data loaded successfully!");
    }

    private void loadRestaurants() throws Exception {
        ClassPathResource resource = new ClassPathResource("initial_data/restaurants.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean isHeader = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                Restaurant restaurant = new Restaurant();
                restaurant.setId(values[0]);
                restaurant.setName(values[1]);
                restaurant.setCuisine(values[2]);
                restaurant.setCity(values[3]);
                restaurant.setRating(Double.parseDouble(values[4]));
                restaurant.setOpen(Boolean.parseBoolean(values[5]));
                restaurant.setCreatedAt(LocalDateTime.parse(values[6], formatter));

                restaurantRepository.save(restaurant);
            }
        }
        System.out.println("Loaded " + restaurantRepository.count() + " restaurants");
    }

    private void loadMenuItems() throws Exception {
        ClassPathResource resource = new ClassPathResource("initial_data/menu_items.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                MenuItem menuItem = new MenuItem();
                menuItem.setId(values[0]);
                menuItem.setRestaurantId(values[1]);
                menuItem.setName(values[2]);
                menuItem.setCategory(values[3]);
                menuItem.setPrice(Double.parseDouble(values[4]));
                menuItem.setAvailable(Boolean.parseBoolean(values[5]));

                menuItemRepository.save(menuItem);
            }
        }
        System.out.println("Loaded " + menuItemRepository.count() + " menu items");
    }
}
