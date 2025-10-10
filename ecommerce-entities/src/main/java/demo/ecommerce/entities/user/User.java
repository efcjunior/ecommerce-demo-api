package demo.ecommerce.entities.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class User {

    private final UUID id;
    private final String name;
    private final String email;
    private final String passwordHash;
    private final UserRole role;
    private final LocalDateTime createdAt;

    private User(UUID id, String name, String email, String passwordHash, UserRole role, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.role = Objects.requireNonNull(role);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        validate();
    }

    public static User register(String name, String email, String hashedPassword, UserRole role) {
        return new User(UUID.randomUUID(), name, email, hashedPassword, role, LocalDateTime.now());
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isUser() {
        return role == UserRole.USER;
    }

    private void validate() {
        if (!email.contains("@")) throw new IllegalArgumentException("Invalid email format");
    }
}
