package ru.otus.software_architect.eshop_notify.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.otus.software_architect.eshop_notify.api.NotifyActionEnum;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

@Component
public class EmailService {
    @Value("${spring.mail.username}")
    private String senderEmailAddress;
    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.application.version}")
    private String appVersion;
    @Autowired
    private JavaMailSender javaMailSender;

    private static Logger logger = LogManager.getLogger(EmailService.class);

    // from email: otus.sa.eshop.alexei@gmail.com
    public void sendEmail(String emailTo, NotifyActionEnum actionEnum, String orderId, String date)
            throws AddressException, MessagingException, IOException {
        String subject = "Order with number " + orderId + " was success";
        switch (actionEnum) {
            case ORDER_CREATE:
                subject = subject + " created.";
                break;
            case ORDER_DELETE:
                subject = subject + " deleted.";
                break;
            case ORDER_UPDATE:
                subject = subject + " updated.";
                break;
            default:
                logger.warn("Unknown type of action = " + actionEnum);
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(senderEmailAddress);
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText("Hello!"
                + "\n" + subject
                + "\nDate: " + date
                + "\n\n" + appName + ".");
        logger.info("senderEmailAddress = " + senderEmailAddress
                + "\nsimpleMailMessage = " + simpleMailMessage);
        javaMailSender.send(simpleMailMessage);
    }
}
