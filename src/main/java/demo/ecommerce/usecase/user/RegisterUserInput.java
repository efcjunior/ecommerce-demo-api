package demo.ecommerce.usecase.user;

public record RegisterUserInput(
        String name,
        String email,
        String password,
        String role
) {}