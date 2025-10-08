package demo.ecommerce.usecase.product;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProductUseCaseImpl implements SearchProductUseCase {

    private final ProductSearchGateway productSearch;

    public SearchProductUseCaseImpl(ProductSearchGateway productSearch) {
        this.productSearch = productSearch;
    }

    @Override
    public SearchProductOutput execute(SearchProductInput input) {
        List<Product> results = productSearch.search(
                input.name(),
                input.category(),
                input.minPrice(),
                input.maxPrice()
        );

        return new SearchProductOutput(results);
    }
}