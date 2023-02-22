package com.example.catalogservice.config;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            private CatalogRepository repository;

            @Override
            public void run(ApplicationArguments args) {
                CatalogEntity entitiy1 = CatalogEntity.builder().createdAt(new Date()).productId("CATALOG-001").productName("Berlin").stock(1000).unitPrice(1500).build();
                CatalogEntity entitiy2 = CatalogEntity.builder().createdAt(new Date()).productId("CATALOG-002").productName("Tokyo").stock(500).unitPrice(1000).build();
                CatalogEntity entitiy3 = CatalogEntity.builder().createdAt(new Date()).productId("CATALOG-003").productName("Stockholm").stock(1500).unitPrice(2000).build();

                repository.save(entitiy1);
                repository.save(entitiy2);
                repository.save(entitiy3);
            }
        };
    }
}
