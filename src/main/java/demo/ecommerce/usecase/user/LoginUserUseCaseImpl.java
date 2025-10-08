package demo.ecommerce.usecase.user;

import demo.ecommerce.entity.User;
import demo.ecommerce.gateway.AuthGateway;
import demo.ecommerce.gateway.UserRepositoryGateway;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private final UserRepositoryGateway userRepository;
    private final AuthGateway authGateway;

    public LoginUserUseCaseImpl(UserRepositoryGateway userRepository, AuthGateway authGateway) {
        this.userRepository = userRepository;
        this.authGateway = authGateway;
    }

    @Override
    public LoginUserOutput execute(LoginUserInput input) {
        User user = userRepository.findByEmail(input.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!authGateway.verifyPassword(input.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = authGateway.generateToken(user);
        return new LoginUserOutput(token, user.getRole());
    }
}