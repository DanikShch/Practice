package practice.internetshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final TemplateEngine templateEngine;

    @Value("${app.reset-password-url}")
    private String resetPasswordUrl;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailUsername);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");
            helper.setText("To reset your password, click: " + resetPasswordUrl.replace("{token}", resetToken));
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailException("Failed to send email", e);
        }
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

    public void sendOrderCancellation(String email, Order order) {
        sendEmail(email, "Order Cancellation", "order-cancellation", order);
    }

    public void sendOrderStatusUpdate(String email, Order order) {
        sendEmail(email, "Order Status Update", "order-status-update", order);
    }

    private void sendEmail(String to, String subject, String templateName, Order order) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject + " #" + order.getId());

            Context context = new Context();
            context.setVariable("order", order);
            context.setVariable("items", order.getItems());

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailException("Failed to send email", e);
        }
    }

    public void sendPasswordChangeConfirmation(String email) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Password Changed Successfully");
            helper.setText("Your password has been successfully changed.");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new  EmailException("Failed to send password change confirmation email", e);
        }
    }
}