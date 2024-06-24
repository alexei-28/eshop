package ru.otus.software_architect.eshop_notify.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.software_architect.eshop_notify.api.Response;
import ru.otus.software_architect.eshop_notify.api.NotifyActionEnum;
import ru.otus.software_architect.eshop_notify.service.EmailService;
import ru.otus.software_architect.eshop_notify.service.ResponseService;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class NotifyEmailController {
    @Autowired
    private EmailService emailService;

    private static Logger logger = LogManager.getLogger(NotifyEmailController.class);

    // Use: http://localhost:8091/api/v1/notifyEmail?emailTo=email@email.com&action=order_update&orderId=10
    /*-
    If you get error (while send email): "535-5.7.8 Username and Password not accepted"
    then:
      1. Login into your account Gmail or Google Apps then goto:https://www.google.com/settings/security/lesssecureapps
      2. and select Turn On for Access for less secure apps.
     */
    @PostMapping("/notifyEmail")
    public Response notifyByEmail(@RequestParam(name = "emailTo") String email,
                                  @RequestParam(name = "action") String action,
                                  @RequestParam(name = "orderId") String orderId
    ) {
        try {
            NotifyActionEnum notifyActionEnum = NotifyActionEnum.valueOf(action.toUpperCase());
            logger.info("notifyByEmail: Sending email...");
            logger.info("notifyByEmail: emailTo = " + email + ", notifyActionEnum = " + notifyActionEnum + ", orderId = " + orderId);
            emailService.sendEmail(email, notifyActionEnum, orderId, "");
            logger.info("notifyByEmail: success_sent_email");
            return ResponseService.getSuccessResponse();
        } catch (MessagingException | IOException ex) {
            logger.error(ex);
            return ResponseService.getErrorResponse(ex.getMessage());
        }
    }
}
