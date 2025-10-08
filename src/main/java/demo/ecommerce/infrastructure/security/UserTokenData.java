package demo.ecommerce.infrastructure.security;

import java.time.Instant;

public record UserTokenData(
        String userId,
        String email,
        String role,
        Instant expiresAt
) {}