package com.fooddelivery.restaurantmenuservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Collections;

@Configuration
@EnableMongoAuditing // Optional, but good for tracking dates
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        // No custom conversions required when using Double for prices.
        return new MongoCustomConversions(Collections.emptyList());
    }
}