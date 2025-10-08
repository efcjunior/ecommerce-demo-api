package demo.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    private final UUID id;
    private final String name;
    private final String email;
    private final String passwordHash;
    private final String role; // ADMIN or USER
    private final LocalDateTime createdAt;
}
