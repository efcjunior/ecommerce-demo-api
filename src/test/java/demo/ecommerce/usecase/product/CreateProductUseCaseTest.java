package demo.ecommerce.usecase.product;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CreateProductUseCaseTest {

    private ProductRepositoryGateway productRepository;
    private ProductSearchGateway productSearch;
    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepositoryGateway.class);
        productSearch = mock(ProductSearchGateway.class);
        useCase = new CreateProductUseCaseImpl(productRepository, productSearch);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // given
        CreateProductInput input = new CreateProductInput(
                "Notebook",
                "Dell XPS 13",
                BigDecimal.valueOf(8500.00),
                "Electronics",
                10
        );

        Product savedProduct = new Product(
                UUID.randomUUID(),
                input.name(),
                input.description(),
                input.price(),
                input.category(),
                input.stockQuantity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // when
        CreateProductOutput output = useCase.execute(input);

        // then
        assertNotNull(output);
        assertNotNull(output.productId());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productSearch, times(1)).index(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        CreateProductInput input = new CreateProductInput(
                "Mouse",
                "Wireless Mouse",
                BigDecimal.ZERO,
                "Electronics",
                5
        );

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        CreateProductInput input = new CreateProductInput(
                "Keyboard",
                "Mechanical Keyboard",
                BigDecimal.valueOf(300.00),
                "Electronics",
                -1
        );

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
    }

}
