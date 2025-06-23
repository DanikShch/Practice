package practice.internetshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import practice.internetshop.exception.user.EmailException;
import practice.internetshop.model.Order;
import org.thymeleaf.context.Context;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService{

    private final JavaMailSender mailSender;
    private final Environment env;
    private final TemplateEngine templateEngine;

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

    public void sendOrderConfirmation(String to, Order order) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Order Confirmation #" + order.getId());

            Context context = new Context();
            context.setVariable("order", order);
            context.setVariable("items", order.getItems());

            String htmlContent = templateEngine.process("order-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailException("Failed to send confirmation email", e);
        }
    }
}