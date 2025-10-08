package demo.ecommerce.controller.dto;

public record LoginUserResponse(
        String token,
        String role
) {}