package ru.otus.software_architect.eshop_message_broker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.software_architect.eshop_message_broker.component_old.Producer;

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

    private static final Logger logger = LogManager.getLogger(HelloController.class);


    // Use: http://localhost:8094/api/v1/hello
    @GetMapping("/hello")
    public Object hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", appName + ", ver." + appVersion);
        map.put("date", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        logger.info("hello: response -> map = " + map);
        return map;
    }
}
