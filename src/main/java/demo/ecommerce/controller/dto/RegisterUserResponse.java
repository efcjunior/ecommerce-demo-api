package demo.ecommerce.controller.dto;

import java.util.UUID;

public record RegisterUserResponse(
        UUID userId,
        String email
) {}