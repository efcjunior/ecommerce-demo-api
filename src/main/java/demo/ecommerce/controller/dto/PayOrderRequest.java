package demo.ecommerce.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record PayOrderRequest(
        @NotBlank String paymentMethod
) {}
