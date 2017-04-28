package com.ixigo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Created by dixant on 24/03/17.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.ixigo")
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer testProperties() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}