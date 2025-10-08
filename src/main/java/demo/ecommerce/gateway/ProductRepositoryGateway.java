package demo.ecommerce.gateway;

import demo.ecommerce.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryGateway {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    void deleteById(UUID id);
    List<Product> findAll();
}