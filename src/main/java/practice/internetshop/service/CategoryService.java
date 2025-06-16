package practice.internetshop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.internetshop.dto.category.CategoryDto;
import practice.internetshop.mapper.CategoryMapper;
import practice.internetshop.model.Category;
import practice.internetshop.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(UUID id) {
        return categoryMapper.toDto(findCategoryById(id));
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);

        if (categoryDto.getParentId() != null) {
            category.setParent(findCategoryById(categoryDto.getParentId()));
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryDto categoryDto) {
        Category existingCategory = findCategoryById(id);

        categoryMapper.updateEntity(categoryDto, existingCategory);

        if (categoryDto.getParentId() != null) {
            existingCategory.setParent(findCategoryById(categoryDto.getParentId()));
        } else {
            existingCategory.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDto(updatedCategory);
    }


    @Transactional(readOnly = true)
    public Category getCategoryEntityById(UUID id) {
        return findCategoryById(id);
    }

    private Category findCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        // Проверяем, есть ли дочерние категории
        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with child categories. Delete children first.");
        }

        // Проверяем, есть ли привязанные продукты
        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated products. Move or delete products first.");
        }

        // Если категория была чьей-то дочерней, удаляем связь
        if (category.getParent() != null) {
            category.getParent().getChildren().remove(category);
        }

        categoryRepository.delete(category);
    }
}