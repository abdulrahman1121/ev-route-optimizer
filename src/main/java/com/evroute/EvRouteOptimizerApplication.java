package com.evroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EvRouteOptimizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvRouteOptimizerApplication.class, args);
    }
}
