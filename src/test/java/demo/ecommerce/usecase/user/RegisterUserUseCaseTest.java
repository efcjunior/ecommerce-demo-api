package demo.ecommerce.usecase.user;

import demo.ecommerce.entity.User;
import demo.ecommerce.gateway.AuthGateway;
import demo.ecommerce.gateway.UserRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseTest {

    private UserRepositoryGateway userRepository;
    private AuthGateway authGateway;
    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryGateway.class);
        authGateway = mock(AuthGateway.class);
        useCase = new RegisterUserUseCaseImpl(userRepository, authGateway);
    }

    @Test
    void shouldRegisterNewUserSuccessfully() {
        RegisterUserInput input = new RegisterUserInput("Alice", "alice@test.com", "123456", "USER");

        when(userRepository.existsByEmail(input.email())).thenReturn(false);
        when(authGateway.hashPassword("123456")).thenReturn("hashed");

        RegisterUserOutput output = useCase.execute(input);

        assertNotNull(output.userId());
        assertEquals(input.email(), output.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterUserInput input = new RegisterUserInput("Bob", "bob@test.com", "123456", "USER");

        when(userRepository.existsByEmail(input.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
        verify(userRepository, never()).save(any());
    }
}