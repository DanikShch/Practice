package practice.internetshop.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import practice.internetshop.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = {"cart"})
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
