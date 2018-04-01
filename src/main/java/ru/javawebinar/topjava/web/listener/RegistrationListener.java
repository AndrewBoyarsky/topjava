package ru.javawebinar.topjava.web.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.mail.EmailService;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.OnRegistrationCompleteEvent;
import ru.javawebinar.topjava.web.MessageUtil;

import java.util.UUID;

@Component
//todo change name of registration listener
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService service;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(token, user.getId());
        String recipientAddress = user.getEmail();
        String subject = messageUtil.getMessage("email.confirmation.subject");
        String confirmationUrl = "http://localhost:8080" + event.getAppUrl() + "/confirmEmail?token=" + token;
        String message = String.format(messageUtil.getMessage("email.confirmation.text"), user.getName(), confirmationUrl);
        emailService.sendSimpleEmail(recipientAddress, subject, message);
    }
}
