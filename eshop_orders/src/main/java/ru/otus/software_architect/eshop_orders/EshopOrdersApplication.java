package ru.otus.software_architect.eshop_orders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class EshopOrdersApplication {
    private static Logger logger = LogManager.getLogger(EshopOrdersApplication.class);

    public static void main(String[] args) {
        logger.info("E-shop orders!");
        logger.info("Java version: " + System.getProperty("java.version"));
        logger.info("Current date: " + new Date());
        SpringApplication.run(EshopOrdersApplication.class, args);
    }

}
