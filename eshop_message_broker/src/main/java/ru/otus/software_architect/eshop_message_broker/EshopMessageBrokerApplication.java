package ru.otus.software_architect.eshop_message_broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import ru.otus.software_architect.eshop_message_broker.model.Product;

@SpringBootApplication
@EnableJms
public class EshopMessageBrokerApplication {
    private static final String MESSAGE_QUEUE = "message_queue";

    public static void main(String[] args) {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication
                .run(EshopMessageBrokerApplication.class, args);

        // Get JMS template bean reference
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        for (int i = 1; i <= 10; i++) {
            Product product = new Product();
            product.setProductId(i);
            product.setName("Laptop");
            product.setQuantity(10 + i);

            // Send a message
            System.out.println("Sending a product " + i);
            jmsTemplate.convertAndSend(MESSAGE_QUEUE, "Hello#"+ i);
        }
    }

}
