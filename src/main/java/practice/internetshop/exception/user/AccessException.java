package practice.internetshop.exception.user;

import org.springframework.security.access.AccessDeniedException;

public class AccessException extends AccessDeniedException {
    public AccessException(String message) {
        super(message);
    }
}
