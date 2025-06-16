package practice.internetshop.mapper;

import practice.internetshop.dto.product.ProductDto;
import practice.internetshop.model.Product;

public interface ProductMapper {
    Product toEntity(ProductDto dto);
    ProductDto toDto(Product entity);
    void updateEntity(ProductDto dto, Product entity);
}