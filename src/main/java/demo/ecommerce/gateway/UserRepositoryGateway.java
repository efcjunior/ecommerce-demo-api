package demo.ecommerce.gateway;

import demo.ecommerce.entity.User;
import java.util.Optional;

public interface UserRepositoryGateway {
    void save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
