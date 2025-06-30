package practice.internetshop.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import practice.internetshop.model.Product;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSpecifications {

    public static Specification<Product> initial() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Product> byCategory(UUID categoryId) {
        return (root, query, cb) ->
                categoryId != null ?
                        cb.equal(root.get("category").get("id"), categoryId) :
                        cb.conjunction();
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice != null ?
                        cb.greaterThanOrEqualTo(root.get("price"), minPrice) :
                        cb.conjunction();
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice != null ?
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice) :
                        cb.conjunction();
    }

    public static Specification<Product> stockGreaterThanOrEqual(Integer minStock) {
        return (root, query, cb) ->
                minStock != null ?
                        cb.greaterThanOrEqualTo(root.get("stockQuantity"), minStock) :
                        cb.conjunction();
    }

    public static Specification<Product> nameContains(String searchQuery) {
        return (root, query, cb) ->
                searchQuery != null && !searchQuery.isEmpty() ?
                        cb.like(cb.lower(root.get("name")), "%" + searchQuery.toLowerCase() + "%") :
                        cb.conjunction();
    }
}