package practice.internetshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import practice.internetshop.dto.order.OrderRequest;
import practice.internetshop.dto.order.OrderResponseDto;
import practice.internetshop.dto.order.OrderStatusUpdateRequest;
import practice.internetshop.model.Order;
import practice.internetshop.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponseDto order = orderService.createOrder(userDetails, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<OrderResponseDto> orders = orderService.getUserOrders(userDetails);
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID orderId
    ) {
        OrderResponseDto order = orderService.getUserOrder(userDetails, orderId);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID orderId
    ) {
        orderService.cancelOrder(userDetails, orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(
            @RequestParam(required = false) UUID userId
    ) {
        if (userId != null) {
            return ResponseEntity.ok(orderService.getOrdersByUser(userId));
        }
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request
    ) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/admin/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> adminCancelOrder(@PathVariable UUID orderId) {
        orderService.adminCancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}