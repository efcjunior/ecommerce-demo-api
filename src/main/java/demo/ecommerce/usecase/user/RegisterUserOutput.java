package demo.ecommerce.usecase.user;

import java.util.UUID;

public record RegisterUserOutput(
        UUID userId,
        String email
) {}