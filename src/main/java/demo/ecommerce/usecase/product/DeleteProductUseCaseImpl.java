package demo.ecommerce.usecase.product;

import demo.ecommerce.gateway.ProductRepositoryGateway;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

    private final ProductRepositoryGateway productRepository;
    private final ProductSearchGateway productSearch;

    public DeleteProductUseCaseImpl(ProductRepositoryGateway productRepository,
                                    ProductSearchGateway productSearch) {
        this.productRepository = productRepository;
        this.productSearch = productSearch;
    }

    @Override
    public DeleteProductOutput execute(DeleteProductInput input) {
        var existing = productRepository.findById(input.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productRepository.deleteById(existing.getId());
        productSearch.deleteById(existing.getId());

        return new DeleteProductOutput(true);
    }
}