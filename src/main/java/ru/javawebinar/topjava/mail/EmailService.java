package ru.javawebinar.topjava.mail;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String content);
}
