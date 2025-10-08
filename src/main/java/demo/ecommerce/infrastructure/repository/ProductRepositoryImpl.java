package demo.ecommerce.infrastructure.repository;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryGateway {

    private final SpringDataProductRepository repository;

    public ProductRepositoryImpl(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        var entity = ProductEntity.fromDomain(product);
        var saved = repository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id).map(ProductEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll()
                .stream()
                .map(ProductEntity::toDomain)
                .collect(Collectors.toList());
    }

}