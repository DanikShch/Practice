package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practice.internetshop.model.PromoCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromoCodeRepository extends JpaRepository<PromoCode, UUID> {
    Optional<PromoCode> findByCode(String code);
    boolean existsByCode(String code);

    @Query("SELECT p FROM PromoCode p WHERE p.isActive = true AND " +
            "(p.expiryDate IS NULL OR p.expiryDate > :now) AND " +
            "(p.usageLimit IS NULL OR p.timesUsed < p.usageLimit)")
    List<PromoCode> findAllValid(LocalDateTime now);

    List<PromoCode> findAllByIsActiveTrueAndExpiryDateAfterOrExpiryDateIsNull(LocalDateTime date);
}
