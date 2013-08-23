package com.ach.test.stub;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;


public class MockMailSender extends JavaMailSenderImpl {

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

    }

    @Override
    public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {

    }

    @Override
    public void send(MimeMessage[] mimeMessages) throws MailException {

    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {

    }
}
