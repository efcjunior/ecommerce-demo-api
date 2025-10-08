package demo.ecommerce.usecase.user;

public record LoginUserOutput(
        String token,
        String role
) {}
