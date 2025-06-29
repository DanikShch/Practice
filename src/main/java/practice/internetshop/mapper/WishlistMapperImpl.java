package practice.internetshop.mapper;

import org.springframework.stereotype.Component;
import practice.internetshop.dto.wishlist.WishlistItemDto;
import practice.internetshop.model.WishlistItem;

@Component
public class WishlistMapperImpl implements WishlistMapper {
    public WishlistItemDto toDto(WishlistItem item) {

        WishlistItemDto dto = new WishlistItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setAddedAt(item.getAddedAt());
        return dto;
    }
}