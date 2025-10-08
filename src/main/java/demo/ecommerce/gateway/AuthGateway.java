package demo.ecommerce.gateway;

import demo.ecommerce.entity.User;

public interface AuthGateway {
    String hashPassword(String rawPassword);
    boolean verifyPassword(String rawPassword, String hashedPassword);
    String generateToken(User user);
}