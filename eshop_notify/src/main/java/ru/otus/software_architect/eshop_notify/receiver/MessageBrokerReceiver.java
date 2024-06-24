package ru.otus.software_architect.eshop_notify.receiver;

import com.google.gson.JsonElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.otus.software_architect.eshop_notify.api.NotifyActionEnum;
import ru.otus.software_architect.eshop_notify.service.EmailService;
import ru.otus.software_architect.eshop_notify.util.GsonUtil;
import ru.otus.software_architect.eshop_notify.util.StringUtil;

import javax.mail.MessagingException;
import java.io.IOException;

/*-
 Receive message from message broker (Apache ActiveMQ)
*/
@Component
public class MessageBrokerReceiver {
    @Autowired
    private EmailService emailService;

    private static final String ESHOP_QUEUE = "eshop_queue";
    private static Logger logger = LogManager.getLogger(MessageBrokerReceiver.class);

    @JmsListener(destination = ESHOP_QUEUE)
    public void receiveMessage(String urlEncodeMessage) {
        try {
            String urlDecodeMessage = StringUtil.urlDecode(urlEncodeMessage);
            String pureDecodeMessage = StringUtil.removeFirstAndLastQuotes(urlDecodeMessage);
            JsonElement messageJson = GsonUtil.parser.parse(pureDecodeMessage);
            logger.info("\nreceiveMessage: Received_message_from_message_broker :" + messageJson);
            String action = messageJson.getAsJsonObject().get("action").getAsString();
            NotifyActionEnum notifyActionEnum = NotifyActionEnum.valueOf(action.toUpperCase());
            String email = messageJson.getAsJsonObject().get("email").getAsString();
            String orderId = messageJson.getAsJsonObject().get("orderId").getAsString();
            String createdAt = messageJson.getAsJsonObject().get("createdAt").getAsString();
            //logger.info("receiveMessage: emailTo = " + email + ", notifyActionEnum = " + notifyActionEnum + ", orderId = " + orderId);
            emailService.sendEmail(email, notifyActionEnum, orderId, createdAt);
            logger.info("receiveMessage: success_sent_email");
        } catch (MessagingException | IOException ex) {
            logger.error(ex);
        }
    }
}
