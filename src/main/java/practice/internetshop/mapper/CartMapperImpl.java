package practice.internetshop.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import practice.internetshop.dto.cart.CartDto;
import practice.internetshop.dto.cart.CartItemDto;
import practice.internetshop.model.Cart;
import practice.internetshop.model.CartItem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartMapperImpl implements CartMapper {

    private final ProductMapper productMapper;

    @Override
    public CartDto toDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartDto dto = new CartDto();
        dto.setId(cart.getId());

        if (cart.getItems() != null) {
            List<CartItemDto> items = cart.getItems().stream()
                    .map(this::toItemDto)
                    .collect(Collectors.toList());
            dto.setItems(items);
        } else {
            dto.setItems(Collections.emptyList());
        }

        return dto;
    }

    @Override
    public CartItemDto toItemDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setProduct(productMapper.toDto(cartItem.getProduct()));
        dto.setQuantity(cartItem.getQuantity());

        return dto;
    }
}