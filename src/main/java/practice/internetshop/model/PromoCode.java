package practice.internetshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "promocodes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {
    public enum DiscountType {
        PERCENTAGE, FIXED_AMOUNT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private LocalDateTime expiryDate;

    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Integer usageLimit;
    private Integer timesUsed = 0;

    @Column(length = 1000)
    private String description;

    public boolean isValid() {
        return isActive &&
                (expiryDate == null || LocalDateTime.now().isBefore(expiryDate)) &&
                (usageLimit == null || timesUsed < usageLimit);
    }

    public void incrementUsage() {
        this.timesUsed++;
    }
}