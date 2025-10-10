package demo.ecommerce.entities.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldRegisterUserWithValidData() {
        User user = User.register("Alice", "alice@example.com", "hashed123", UserRole.USER);

        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("hashed123", user.getPasswordHash());
        assertEquals(UserRole.USER, user.getRole());
    }


    @Test
    void shouldSetIdAndCreatedAtOnRegister() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        User user = User.register("Bob", "bob@example.com", "h", UserRole.ADMIN);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertNotNull(user.getId());
        assertNotEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), user.getId());
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isAfter(before) || user.getCreatedAt().isEqual(before));
        assertTrue(user.getCreatedAt().isBefore(after) || user.getCreatedAt().isEqual(after));
    }


    @ParameterizedTest
    @ValueSource(strings = {"invalid", "userexample.com", "alice.example.com", "no-at-sign"})
    void shouldThrowExceptionWhenEmailHasInvalidFormat(String invalidEmail) {
        assertThrows(IllegalArgumentException.class,
                () -> User.register("Alice", invalidEmail, "hash", UserRole.USER));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(NullPointerException.class,
                () -> User.register(null, "a@b.com", "hash", UserRole.USER));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThrows(NullPointerException.class,
                () -> User.register("Name", null, "hash", UserRole.USER));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHashIsNull() {
        assertThrows(NullPointerException.class,
                () -> User.register("Name", "a@b.com", null, UserRole.USER));
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        assertThrows(NullPointerException.class,
                () -> User.register("Name", "a@b.com", "hash", null));
    }

    @Test
    void shouldReturnTrueForIsAdminWhenRoleAdmin() {
        User admin = User.register("Admin", "admin@x.com", "h", UserRole.ADMIN);
        assertTrue(admin.isAdmin());
        assertFalse(admin.isUser());
    }

    @Test
    void shouldReturnTrueForIsUserWhenRoleUser() {
        User user = User.register("User", "user@x.com", "h", UserRole.USER);
        assertTrue(user.isUser());
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldReturnFalseForIsAdminWhenRoleUser() {
        User user = User.register("User", "u@x.com", "h", UserRole.USER);
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldReturnFalseForIsUserWhenRoleAdmin() {
        User admin = User.register("Admin", "a@x.com", "h", UserRole.ADMIN);
        assertFalse(admin.isUser());
    }

    @Test
    void shouldGenerateDifferentIdsForDifferentRegistrations() {
        User u1 = User.register("A", "a@x.com", "h1", UserRole.USER);
        User u2 = User.register("B", "b@x.com", "h2", UserRole.ADMIN);
        assertNotEquals(u1.getId(), u2.getId());
    }

    @Test
    void shouldBeImmutable() {
        Field[] fields = User.class.getDeclaredFields();
        for (Field f : fields) {
            int mod = f.getModifiers();
            if (!Modifier.isStatic(mod)) {
                assertTrue(Modifier.isFinal(mod), "Field must be final: " + f.getName());
            }
        }
    }

}
