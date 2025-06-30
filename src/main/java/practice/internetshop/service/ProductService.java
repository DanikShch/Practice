package practice.internetshop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.internetshop.dto.product.ProductDto;
import practice.internetshop.dto.product.ProductFilterRequest;
import practice.internetshop.mapper.ProductMapper;
import practice.internetshop.model.Product;
import practice.internetshop.model.ProductImage;
import practice.internetshop.repository.ProductRepository;
import practice.internetshop.repository.specification.ProductSpecifications;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public List<ProductDto> getFilteredProducts(ProductFilterRequest filter) {
        Specification<Product> spec = buildSpecification(filter);
        Sort sort = buildSort(filter);

        return productRepository.findAll(spec, sort)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID id) {
        return productMapper.toDto(findProductById(id));
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);

        if (productDto.getCategoryId() != null) {
            product.setCategory(categoryService.getCategoryEntityById(productDto.getCategoryId()));
        }

        if (productDto.getImages() != null) {
            productDto.getImages().forEach(dto -> {
                ProductImage image = new ProductImage();
                image.setImageUrl(dto.getImageUrl());
                image.setIsPrimary(dto.getIsPrimary());
                image.setProduct(product);
                product.getImages().add(image);
            });
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(UUID id, ProductDto productDto) {
        Product existingProduct = findProductById(id);

        productMapper.updateEntity(productDto, existingProduct);

        if (productDto.getCategoryId() != null) {
            existingProduct.setCategory(categoryService.getCategoryEntityById(productDto.getCategoryId()));
        } else {
            existingProduct.setCategory(null);
        }

        if (productDto.getImages() != null) {
            existingProduct.getImages().clear();
            productDto.getImages().forEach(dto -> {
                ProductImage image = new ProductImage();
                image.setImageUrl(dto.getImageUrl());
                image.setIsPrimary(dto.getIsPrimary());
                image.setProduct(existingProduct);
                existingProduct.getImages().add(image);
            });
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }


    private Product findProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    private Specification<Product> buildSpecification(ProductFilterRequest filter) {
        return ProductSpecifications.initial()
                .and(ProductSpecifications.byCategory(filter.getCategoryId()))
                .and(ProductSpecifications.priceGreaterThanOrEqual(filter.getMinPrice()))
                .and(ProductSpecifications.priceLessThanOrEqual(filter.getMaxPrice()))
                .and(ProductSpecifications.stockGreaterThanOrEqual(filter.getMinStock()))
                .and(ProductSpecifications.nameContains(filter.getSearchQuery()));
    }

    private Sort buildSort(ProductFilterRequest filter) {
        String sortField = filter.getSortBy() != null ? filter.getSortBy() : "name";
        Sort.Direction direction = filter.getDirection() != null ?
                filter.getDirection() : Sort.Direction.ASC;
        return Sort.by(direction, sortField);
    }
}