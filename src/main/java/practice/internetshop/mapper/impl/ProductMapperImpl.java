package practice.internetshop.mapper.impl;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.product.ProductDto;
import practice.internetshop.dto.product.ProductImageDto;
import practice.internetshop.mapper.ProductMapper;
import practice.internetshop.model.Product;
import practice.internetshop.model.ProductImage;

import java.util.stream.Collectors;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    @Override
    public ProductDto toDto(Product entity) {
        if (entity == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
        }

        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            dto.setImages(entity.getImages().stream()
                    .map(this::toImageDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private ProductImageDto toImageDto(ProductImage image) {
        ProductImageDto dto = new ProductImageDto();
        dto.setImageUrl(image.getImageUrl());
        dto.setIsPrimary(image.getIsPrimary());
        return dto;
    }

    @Override
    public void updateEntity(ProductDto dto, Product entity) {
        if (dto == null || entity == null) return;

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStockQuantity(dto.getStockQuantity());
    }
}