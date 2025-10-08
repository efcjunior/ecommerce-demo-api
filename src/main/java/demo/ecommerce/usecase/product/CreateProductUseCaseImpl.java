package demo.ecommerce.usecase.product;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final ProductRepositoryGateway productRepository;
    private final ProductSearchGateway productSearch;

    public CreateProductUseCaseImpl(ProductRepositoryGateway productRepository,
                                    ProductSearchGateway productSearch) {
        this.productRepository = productRepository;
        this.productSearch = productSearch;
    }

    @Override
    public CreateProductOutput execute(CreateProductInput input) {
        if (input.price() == null || input.price().doubleValue() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (input.stockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        Product product = new Product(
                UUID.randomUUID(),
                input.name(),
                input.description(),
                input.price(),
                input.category(),
                input.stockQuantity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product saved = productRepository.save(product);
        productSearch.index(saved);

        return new CreateProductOutput(saved.getId(), saved.getCreatedAt());
    }

}