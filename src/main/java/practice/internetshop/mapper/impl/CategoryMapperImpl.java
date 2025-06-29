package practice.internetshop.mapper.impl;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.category.CategoryDto;
import practice.internetshop.mapper.CategoryMapper;
import practice.internetshop.model.Category;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        category.setName(dto.getName());
        // Родительская категория устанавливается в сервисе
        return category;
    }

    @Override
    public CategoryDto toDto(Category entity) {
        if (entity == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
        }

        return dto;
    }

    @Override
    public void updateEntity(CategoryDto dto, Category entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        // Родительская категория обновляется в сервисе
    }
}