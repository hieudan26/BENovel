package mobile.Service.Impl;

import mobile.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${apps.server.host}")
    private String host;

    @Override
    public void sendSimpleMessage(
            String to, String code) {
        SimpleMailMessage message = templateRegisterMessage(code);
        message.setFrom("server2.noreply@gmail.com");
        message.setTo(to);
        emailSender.send(message);

    }


    @Override
    public void sendMessageWithAttachment(
            String to, String subject, String text, String pathToAttachment) {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("noreply@baeldung.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
         emailSender.send(message);
    }


    public SimpleMailMessage templateRegisterMessage(String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        String validationLink = host+"api/auth/active?key="+code;
        message.setSubject("Validation register");
        message.setText(validationLink);
        return message;
    }
}