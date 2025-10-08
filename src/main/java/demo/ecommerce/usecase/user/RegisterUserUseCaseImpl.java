package demo.ecommerce.usecase.user;

import demo.ecommerce.entity.User;
import demo.ecommerce.gateway.AuthGateway;
import demo.ecommerce.gateway.UserRepositoryGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryGateway userRepository;
    private final AuthGateway authGateway;

    public RegisterUserUseCaseImpl(UserRepositoryGateway userRepository, AuthGateway authGateway) {
        this.userRepository = userRepository;
        this.authGateway = authGateway;
    }

    @Override
    public RegisterUserOutput execute(RegisterUserInput input) {
        if (userRepository.existsByEmail(input.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = authGateway.hashPassword(input.password());

        User user = new User(
                UUID.randomUUID(),
                input.name(),
                input.email(),
                hashedPassword,
                input.role(),
                LocalDateTime.now()
        );

        userRepository.save(user);
        return new RegisterUserOutput(user.getId(), user.getEmail());
    }
}