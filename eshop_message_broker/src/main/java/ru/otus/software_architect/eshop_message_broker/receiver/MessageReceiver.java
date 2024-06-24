package ru.otus.software_architect.eshop_message_broker.receiver;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
    private static final String MESSAGE_QUEUE = "message_queue";

    @JmsListener(destination = MESSAGE_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Received " + message);
    }
}
