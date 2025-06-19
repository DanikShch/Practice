package practice.internetshop.mapper;

import practice.internetshop.dto.cart.CartDto;
import practice.internetshop.dto.cart.CartItemDto;
import practice.internetshop.model.Cart;
import practice.internetshop.model.CartItem;

public interface CartMapper {
    CartDto toDto(Cart cart);
    CartItemDto toItemDto(CartItem cartItem);
}