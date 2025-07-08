package org.example.event.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailService {
    private final Session session;

    public EmailService(String username, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("shosha202002@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }
}