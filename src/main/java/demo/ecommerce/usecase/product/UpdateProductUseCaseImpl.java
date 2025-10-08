package demo.ecommerce.usecase.product;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {

    private final ProductRepositoryGateway productRepository;
    private final ProductSearchGateway productSearch;

    public UpdateProductUseCaseImpl(ProductRepositoryGateway productRepository,
                                    ProductSearchGateway productSearch) {
        this.productRepository = productRepository;
        this.productSearch = productSearch;
    }

    @Override
    @Transactional
    public UpdateProductOutput execute(UpdateProductInput input) {
        if (input.price() == null || input.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (input.stockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        var existing = productRepository.findById(input.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        var updated = new Product(
                existing.getId(),
                input.name(),
                input.description(),
                input.price(),
                input.category(),
                input.stockQuantity(),
                existing.getCreatedAt(),
                LocalDateTime.now()
        );

        var saved = productRepository.save(updated);
        productSearch.index(saved);

        return new UpdateProductOutput(saved.getId(), saved.getUpdatedAt());
    }


}