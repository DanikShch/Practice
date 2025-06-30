package practice.internetshop.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.internetshop.model.User;
import practice.internetshop.service.CsvExportService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class CsvExportController {

    private final CsvExportService csvExportService;

    @GetMapping("/products")
    public void exportProducts(HttpServletResponse response) throws IOException {
        csvExportService.exportAllProductsToCsv(response);
    }

    @GetMapping("/orders")
    public void exportOrders(HttpServletResponse response) throws IOException {
        csvExportService.exportAllOrdersToCsv(response);
    }

    @GetMapping("/promo")
    public void exportPromoCodes(HttpServletResponse response) throws IOException {
        csvExportService.exportAllPromoCodesToCsv(response);
    }

    @GetMapping("/reviews")
    public void exportReviews(HttpServletResponse response) throws IOException {
        csvExportService.exportAllReviewsToCsv(response);
    }

    @GetMapping("/reviews/{productId}")
    public void exportProductReviews(@PathVariable UUID productId, HttpServletResponse response) throws IOException {
        csvExportService.exportProductReviewsToCsv(productId, response);
    }

    @GetMapping("/users")
    public void exportAllUsers(HttpServletResponse response) throws IOException {
        csvExportService.exportAllUsersToCsv(response);
    }

    @GetMapping("/users/{role}")
    public void exportUsersByRole(@PathVariable User.Role role,
            HttpServletResponse response) throws IOException {
        csvExportService.exportUsersByRoleToCsv(role, response);
    }

}