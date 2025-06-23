package practice.internetshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender mailSender;
    private final Environment env;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetUrl = Objects.requireNonNull(env.getProperty("app.reset-password-url"))
                .replace("{token}", resetToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("spring.mail.username"));
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetUrl);

        mailSender.send(message);
    }
}