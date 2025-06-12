package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.Category;

import java.util.UUID;

public interface CategoryRepository  extends JpaRepository<Category, UUID> {
}
