package ru.otus.software_architect.eshop_notify;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class EshopNotifyApplication {
    private static Logger logger = LogManager.getLogger(EshopNotifyApplication.class);

    public static void main(String[] args) {
        logger.info("E-shop nofification!");
        logger.info("Java version: " + System.getProperty("java.version"));
        logger.info("Current date: " + new Date());
        SpringApplication.run(EshopNotifyApplication.class, args);
    }

}
