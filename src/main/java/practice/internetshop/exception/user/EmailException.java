package practice.internetshop.exception.user;

import jakarta.mail.MessagingException;

public class EmailException extends RuntimeException {
    public EmailException(String message, MessagingException e) {
        super(message);
    }
}
