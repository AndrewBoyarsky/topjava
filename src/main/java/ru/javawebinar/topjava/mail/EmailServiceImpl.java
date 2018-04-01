package ru.javawebinar.topjava.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
    private MailSender mailSender;

    @Autowired
    public EmailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    //sendgrid doesnt accept "From header" if  mail.smtp.from and mail.from properties are using from Java Mail properties map in MailSender
    @Value("${mail.username}")
    private  String FROM;
    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(to);
        emailMessage.setFrom(FROM);
        emailMessage.setSubject(subject);
        emailMessage.setSubject(subject);
        emailMessage.setText(content);
        mailSender.send(emailMessage);
    }
}
