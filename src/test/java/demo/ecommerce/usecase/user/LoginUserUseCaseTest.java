package demo.ecommerce.usecase.user;

import demo.ecommerce.entity.User;
import demo.ecommerce.gateway.AuthGateway;
import demo.ecommerce.gateway.UserRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUserUseCaseTest {

    private UserRepositoryGateway userRepository;
    private AuthGateway authGateway;
    private LoginUserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryGateway.class);
        authGateway = mock(AuthGateway.class);
        useCase = new LoginUserUseCaseImpl(userRepository, authGateway);
    }

    @Test
    void shouldLoginSuccessfullyWhenCredentialsAreValid() {
        User user = new User(
                UUID.randomUUID(),
                "Alice",
                "alice@test.com",
                "hashed",
                "USER",
                LocalDateTime.now()
        );

        when(userRepository.findByEmail("alice@test.com")).thenReturn(Optional.of(user));
        when(authGateway.verifyPassword("123456", "hashed")).thenReturn(true);
        when(authGateway.generateToken(user)).thenReturn("jwt-token");

        LoginUserInput input = new LoginUserInput("alice@test.com", "123456");
        LoginUserOutput output = useCase.execute(input);

        assertEquals("jwt-token", output.token());
        assertEquals("USER", output.role());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        when(userRepository.findByEmail("wrong@test.com")).thenReturn(Optional.empty());

        LoginUserInput input = new LoginUserInput("wrong@test.com", "123456");
        assertThrows(BadCredentialsException.class, () -> useCase.execute(input));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        User user = new User(
                UUID.randomUUID(),
                "Bob",
                "bob@test.com",
                "hashed",
                "USER",
                LocalDateTime.now()
        );

        when(userRepository.findByEmail("bob@test.com")).thenReturn(Optional.of(user));
        when(authGateway.verifyPassword("wrongpass", "hashed")).thenReturn(false);

        LoginUserInput input = new LoginUserInput("bob@test.com", "wrongpass");
        assertThrows(BadCredentialsException.class, () -> useCase.execute(input));
    }
}