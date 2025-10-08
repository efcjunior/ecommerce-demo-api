package demo.ecommerce.infrastructure.repository;

import demo.ecommerce.entity.User;
import demo.ecommerce.infrastructure.converter.UUIDAttributeConverter;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Convert(converter = UUIDAttributeConverter.class)
    private UUID id;
    private String name;
    private String email;
    private String passwordHash;
    private String role;
    private LocalDateTime createdAt;

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User toDomain() {
        return new User(id, name, email, passwordHash, role, createdAt);
    }
}