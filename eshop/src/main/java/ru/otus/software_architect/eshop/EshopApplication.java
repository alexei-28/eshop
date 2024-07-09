package ru.otus.software_architect.eshop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;
/*-
    Open on the next address:
    http://127.0.0.1:8090/

    Default admin credentials(admin@admin.com) set in the class UsersController.
 */
@SpringBootApplication
@EnableAsync
public class EshopApplication {
    private static Logger logger = LogManager.getLogger(EshopApplication.class);

    public static void main(String[] args) {
        logger.info("E-shop!");
        logger.info("Java version: " + System.getProperty("java.version"));
        logger.info("Current date: " + new Date());
        SpringApplication.run(EshopApplication.class, args);
    }


    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

}
