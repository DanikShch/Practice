package practice.internetshop.mapper;

import practice.internetshop.dto.wishlist.WishlistItemDto;
import practice.internetshop.model.WishlistItem;

public interface WishlistMapper {
    public WishlistItemDto toDto(WishlistItem item);
}
