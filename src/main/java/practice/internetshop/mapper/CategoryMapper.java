package practice.internetshop.mapper;

import practice.internetshop.dto.category.CategoryDto;
import practice.internetshop.model.Category;

public interface CategoryMapper {
    Category toEntity(CategoryDto dto);
    CategoryDto toDto(Category entity);
    void updateEntity(CategoryDto dto, Category entity);
}