package practice.internetshop.model;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public boolean canBeCancelled() {
        return this == CREATED || this == CONFIRMED;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        // Простые правила перехода статусов
        return switch (this) {
            case CREATED -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == SHIPPED;
            case SHIPPED -> newStatus == DELIVERED;
            default -> false;
        };
    }

    public boolean shouldNotifyUser() {
        return this == SHIPPED || this == DELIVERED;
    }
}