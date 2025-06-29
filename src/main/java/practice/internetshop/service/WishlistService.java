package practice.internetshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.internetshop.dto.wishlist.AddToWishlistRequest;
import practice.internetshop.dto.wishlist.WishlistItemDto;
import practice.internetshop.exception.product.ProductAlreadyInWishlistException;
import practice.internetshop.exception.product.ProductNotFoundException;
import practice.internetshop.exception.product.ProductNotInWishlistException;
import practice.internetshop.exception.user.UserNotFoundException;
import practice.internetshop.mapper.WishlistMapper;
import practice.internetshop.model.Product;
import practice.internetshop.model.User;
import practice.internetshop.model.WishlistItem;
import practice.internetshop.repository.ProductRepository;
import practice.internetshop.repository.UserRepository;
import practice.internetshop.repository.WishlistRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WishlistMapper wishlistMapper;

    @Transactional
    public WishlistItemDto addToWishlist(UUID userId, AddToWishlistRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        if (wishlistRepository.existsByUserIdAndProductId(userId, request.getProductId())) {
            throw new ProductAlreadyInWishlistException(request.getProductId());
        }

        WishlistItem item = WishlistItem.builder()
                .user(user)
                .product(product)
                .build();

        return wishlistMapper.toDto(wishlistRepository.save(item));
    }

    @Transactional(readOnly = true)
    public List<WishlistItemDto> getUserWishlist(UUID userId) {
        return wishlistRepository.findByUserId(userId).stream()
                .map(wishlistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromWishlist(UUID userId, UUID productId) {
        if (!wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new ProductNotInWishlistException(productId);
        }
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional(readOnly = true)
    public int getWishlistItemCount(UUID userId) {
        return wishlistRepository.countByUserId(userId);
    }
}
