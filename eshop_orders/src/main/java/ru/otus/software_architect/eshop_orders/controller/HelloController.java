package ru.otus.software_architect.eshop_orders.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.application.version}")
    private String appVersion;

    private static Logger logger = LogManager.getLogger(HelloController.class);

    // Use: http://localhost:8092/api/v1/hello
    @GetMapping("/hello")
    public Object hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", appName + ", ver." + appVersion);
        map.put("date", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        logger.info("hello: response -> map = " + map);
        return map;
    }
}
