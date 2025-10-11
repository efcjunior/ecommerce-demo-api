package demo.ecommerce.usecases.ports;

import demo.ecommerce.entities.product.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findById(UUID id);
    Product save(Product product);
    void delete(UUID id);
    List<Product> search(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);
}
