package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.PromoCode;

import java.util.UUID;

public interface PromoCodeRepository extends JpaRepository<PromoCode, UUID> {
}
