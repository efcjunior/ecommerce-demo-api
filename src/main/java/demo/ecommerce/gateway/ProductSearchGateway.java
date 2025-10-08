package demo.ecommerce.gateway;

import demo.ecommerce.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductSearchGateway {
    void index(Product product);
    void deleteById(UUID id);
    List<Product> search(String name, String category, Double minPrice, Double maxPrice);

}