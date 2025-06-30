package practice.internetshop.service;

import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.internetshop.dto.order.OrderItemDto;
import practice.internetshop.dto.order.OrderResponseDto;
import practice.internetshop.dto.product.ProductDto;
import practice.internetshop.dto.product.ProductImageDto;
import practice.internetshop.dto.promo.PromoCodeDto;
import practice.internetshop.dto.review.ReviewDto;
import practice.internetshop.dto.user.UserDto;
import practice.internetshop.exception.export.CsvExportException;
import practice.internetshop.model.Product;
import practice.internetshop.model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CsvExportService {
    private final ProductService productService;
    private final OrderService orderService;
    private final ProductReviewService reviewService;
    private final PromoCodeService promoCodeService;
    private final UserService userService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String EMPTY_VALUE = "";

    public void exportAllProductsToCsv(HttpServletResponse response) {
        try {
            List<ProductDto> products = productService.getAllProducts();
            writeProductsToCsv(products, response);
        } catch (Exception e) {
            throw new CsvExportException("Failed to export products", e);
        }
    }

    private void writeProductsToCsv(List<ProductDto> products, HttpServletResponse response)  {
        try {
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"products_export.csv\"");

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
                writer.writeNext(new String[]{
                        "ID", "Name", "Description", "Price",
                        "Stock quantity", "Category ID",
                        "Primary image", "Image count"
                });

                for (ProductDto product : products) {
                    writer.writeNext(new String[]{
                            safeToString(product.getId()),
                            escapeCsvSpecialChars(product.getName()),
                            escapeCsvSpecialChars(product.getDescription()),
                            safeBigDecimalToString(product.getPrice()),
                            safeIntegerToString(product.getStockQuantity()),
                            safeToString(product.getCategoryId()),
                            getPrimaryImageUrl(product),
                            safeIntegerToString(product.getImages() != null ? product.getImages().size() : 0)
                    });
                }
            }
        } catch (IOException e) {
            throw new CsvExportException("Error writing products CSV", e);
        }
    }

    public void exportAllOrdersToCsv(HttpServletResponse response) {
        try {
            List<OrderResponseDto> orders = orderService.getAllOrders();
            writeOrdersToCsv(orders, response);
        } catch (Exception e) {
            throw new CsvExportException("Failed to export orders", e);
        }
    }

    private void writeOrdersToCsv(List<OrderResponseDto> orders, HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"orders_export.csv\"");

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
                writer.writeNext(new String[]{
                        "Order ID", "Order Date", "User Email", "Status",
                        "Original Amount", "Discount Amount", "Total Amount",
                        "Promo Code", "Payment Method", "Delivery Method",
                        "Delivery Address", "Product Items"
                });

                for (OrderResponseDto order : orders) {
                    writer.writeNext(new String[]{
                            safeToString(order.getId()),
                            formatDateTime(order.getOrderDate()),
                            safeToString(order.getUserEmail()),
                            safeToString(order.getStatus()),
                            safeBigDecimalToString(order.getOriginalAmount()),
                            safeBigDecimalToString(order.getDiscountAmount()),
                            safeBigDecimalToString(order.getTotalAmount()),
                            safeToString(order.getAppliedPromoCode()),
                            safeToString(order.getPaymentMethod()),
                            safeToString(order.getDeliveryMethod()),
                            safeToString(order.getDeliveryAddress()),
                            formatOrderItems(order.getItems())
                    });
                }
            }
        } catch (IOException e) {
            throw new CsvExportException("Error writing orders CSV", e);
        }
    }

    private String getPrimaryImageUrl(ProductDto product) {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            return "";
        }
        return product.getImages().stream()
                .filter(ProductImageDto::getIsPrimary)
                .findFirst()
                .map(ProductImageDto::getImageUrl)
                .orElse("");
    }

    private String escapeCsvSpecialChars(String value) {
        if (value == null) return EMPTY_VALUE;
        return value.replace("\"", "\"\"")
                .replace("\n", " ")
                .replace("\r", "");
    }


    private String formatOrderItems(List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) {
            return EMPTY_VALUE;
        }

        StringBuilder sb = new StringBuilder();
        for (OrderItemDto item : items) {
            if (item != null) {
                sb.append(String.format(
                        "[ID: %s, Name: %s, Qty: %d, Price: %s]; ",
                        safeToString(item.getProductId()),
                        escapeCsvSpecialChars(safeToString(item.getProductName())),
                        item.getQuantity() != null ? item.getQuantity() : 0,
                        safeBigDecimalToString(item.getUnitPrice())
                ));
            }
        }
        return sb.toString().trim();
    }

    public void exportAllPromoCodesToCsv(HttpServletResponse response) {
        try {
            List<PromoCodeDto> promoCodes = promoCodeService.getAllPromoCodes();
            writePromoCodesToCsv(promoCodes, response);
        } catch (Exception e) {
            throw new CsvExportException("Failed to export promo codes", e);
        }
    }

    private void writePromoCodesToCsv(List<PromoCodeDto> promoCodes, HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"promocodes_export.csv\"");

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
                writer.writeNext(new String[]{
                        "ID",
                        "Promo Code",
                        "Discount Value",
                        "Discount Type",
                        "Expiry Date",
                        "Is Active",
                        "Usage Limit",
                        "Times Used",
                        "Description",
                        "Created At"
                });

                for (PromoCodeDto promoCode : promoCodes) {
                    writer.writeNext(new String[]{
                            safeToString(promoCode.getId()),
                            escapeCsvSpecialChars(promoCode.getCode()),
                            safeBigDecimalToString(promoCode.getDiscountValue()),
                            promoCode.getDiscountType() != null ? promoCode.getDiscountType().name() : EMPTY_VALUE,
                            promoCode.getExpiryDate() != null ? promoCode.getExpiryDate().format(DATE_FORMATTER) : EMPTY_VALUE,
                            safeBooleanToString(promoCode.getIsActive()),
                            safeIntegerToString(promoCode.getUsageLimit()),
                            safeIntegerToString(promoCode.getTimesUsed()),
                            escapeCsvSpecialChars(promoCode.getDescription()),
                            promoCode.getCreatedAt() != null ? promoCode.getCreatedAt().format(DATE_FORMATTER) : EMPTY_VALUE
                    });
                }
            }
        } catch (IOException e) {
            throw new CsvExportException("Error writing promo codes CSV", e);
        }
    }

    public void exportAllReviewsToCsv(HttpServletResponse response) {
        try {
            List<ReviewDto> reviews = reviewService.getAllReviews();
            writeReviewsToCsv(reviews, response);
        } catch (Exception e) {
            throw new CsvExportException("Failed to export reviews", e);
        }
    }

    public void exportProductReviewsToCsv(UUID productId, HttpServletResponse response) {
        try {
            List<ReviewDto> reviews = reviewService.getProductReviews(productId);
            writeReviewsToCsv(reviews, response);
        } catch (Exception e) {
            throw new CsvExportException("Failed to export product reviews", e);
        }
    }

    private void writeReviewsToCsv(List<ReviewDto> reviews, HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"reviews_export.csv\"");

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
                writer.writeNext(new String[]{
                        "Review ID",
                        "Product ID",
                        "User ID",
                        "User Name",
                        "Rating",
                        "Comment",
                        "Created At",
                        "Updated At"
                });

                for (ReviewDto review : reviews) {
                    writer.writeNext(new String[]{
                            safeToString(review.getId()),
                            safeToString(review.getProductId()),
                            safeToString(review.getUserId()),
                            escapeCsvSpecialChars(review.getUserDisplayName()),
                            safeRatingToString(review.getRating()),
                            escapeCsvSpecialChars(review.getComment()),
                            formatDateTime(review.getCreatedAt()),
                            formatDateTime(review.getUpdatedAt())
                    });
                }
            }
        } catch (IOException e) {
            throw new CsvExportException("Error writing reviews CSV", e);
        }
    }


    public void exportUsersByRoleToCsv(User.Role role, HttpServletResponse response){
        try {
            List<UserDto> users = userService.getUsersByRole(role);
            writeUsersToCsv(users, response);
        } catch (Exception e) {
            throw new CsvExportException("Failed to export users", e);
        }
    }

    public void exportAllUsersToCsv(HttpServletResponse response) {
        try {
            List<UserDto> users = userService.getAllUsers();
            writeUsersToCsv(users, response);
        } catch (Exception e) {
            throw new CsvExportException("Failed to export users", e);
        }
    }

    private void writeUsersToCsv(List<UserDto> users, HttpServletResponse response) {
        try {
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"users_export.csv\"");

            try (CSVWriter writer = new CSVWriter(response.getWriter())) {
                writer.writeNext(new String[]{
                        "User ID", "Email", "Role", "Created At",
                        "Orders Count", "Reviews Count"
                });

                for (UserDto user : users) {
                    writer.writeNext(new String[]{
                            safeToString(user.getId()),
                            escapeCsvSpecialChars(user.getEmail()),
                            safeToString(user.getRole()),
                            formatDateTime(user.getCreatedAt()),
                            safeIntegerToString(user.getOrdersCount()),
                            safeIntegerToString(user.getReviewsCount()),
                    });
                }
            }
        } catch (IOException e) {
            throw new CsvExportException("Error writing users CSV", e);
        }
    }


    private String safeToString(Object obj) {
        return obj != null ? obj.toString() : EMPTY_VALUE;
    }

    private String safeBigDecimalToString(BigDecimal value) {
        return value != null ? value.toString() : EMPTY_VALUE;
    }

    private String safeBooleanToString(Boolean value) {
        return value != null ? value.toString() : EMPTY_VALUE;
    }

    private String safeIntegerToString(Integer value) {
        return value != null ? value.toString() : EMPTY_VALUE;
    }

    private String safeRatingToString(Integer rating) {
        if (rating == null) return EMPTY_VALUE;
        return rating + "/5";
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_FORMATTER) : EMPTY_VALUE;
    }

}
