package demo.ecommerce.usecase.product;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductRepositoryGateway;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductUseCaseTest {

    private ProductRepositoryGateway productRepository;
    private ProductSearchGateway productSearch;
    private UpdateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepositoryGateway.class);
        productSearch = mock(ProductSearchGateway.class);
        useCase = new UpdateProductUseCaseImpl(productRepository, productSearch);
    }

    @Test
    void shouldUpdateExistingProductSuccessfully() {
        // given
        UUID id = UUID.randomUUID();
        Product existing = new Product(
                id, "Phone", "Old desc", BigDecimal.valueOf(1000),
                "Electronics", 5, LocalDateTime.now(), LocalDateTime.now()
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));

        UpdateProductInput input = new UpdateProductInput(
                id, "Phone X", "New desc", BigDecimal.valueOf(1200),
                "Electronics", 10
        );

        Product updated = new Product(
                id, input.name(), input.description(), input.price(),
                input.category(), input.stockQuantity(),
                existing.getCreatedAt(), LocalDateTime.now()
        );

        when(productRepository.save(any(Product.class))).thenReturn(updated);

        // when
        UpdateProductOutput output = useCase.execute(input);

        // then
        assertNotNull(output);
        verify(productRepository).findById(id);
        verify(productRepository).save(any(Product.class));
        verify(productSearch).index(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        UpdateProductInput input = new UpdateProductInput(
                id, "X", "Desc", BigDecimal.TEN, "Cat", 5
        );

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id))
                .thenReturn(Optional.of(mock(Product.class)));

        UpdateProductInput input = new UpdateProductInput(
                id, "Phone", "Desc", BigDecimal.ZERO, "Electronics", 5
        );

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id))
                .thenReturn(Optional.of(mock(Product.class)));

        UpdateProductInput input = new UpdateProductInput(
                id, "Phone", "Desc", BigDecimal.valueOf(500), "Electronics", -1
        );

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
    }

}