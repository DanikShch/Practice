package practice.internetshop.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import practice.internetshop.model.Product;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @EntityGraph(attributePaths = {"images"})
    List<Product> findByCategoryId(UUID categoryId);
    @EntityGraph(attributePaths = {"images"})
    List<Product> findByNameContainingIgnoreCase(String name);
}
