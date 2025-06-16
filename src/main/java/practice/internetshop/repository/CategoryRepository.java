package practice.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.internetshop.model.Category;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, UUID> {
    List<Category> findByParentId(UUID parentId);
    boolean existsByNameAndParentId(String name, UUID parentId);
}
