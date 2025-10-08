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

class DeleteProductUseCaseTest {

    private ProductRepositoryGateway productRepository;
    private ProductSearchGateway productSearch;
    private DeleteProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepositoryGateway.class);
        productSearch = mock(ProductSearchGateway.class);
        useCase = new DeleteProductUseCaseImpl(productRepository, productSearch);
    }

    @Test
    void shouldDeleteExistingProductSuccessfully() {
        UUID id = UUID.randomUUID();
        Product existing = new Product(
                id, "Phone", "Smartphone", BigDecimal.valueOf(1200),
                "Electronics", 5, LocalDateTime.now(), LocalDateTime.now()
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));

        DeleteProductInput input = new DeleteProductInput(id);

        DeleteProductOutput output = useCase.execute(input);

        assertTrue(output.success());
        verify(productRepository).findById(id);
        verify(productRepository).deleteById(id);
        verify(productSearch).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        DeleteProductInput input = new DeleteProductInput(id);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(input));
        verify(productRepository, never()).deleteById(any());
        verify(productSearch, never()).deleteById(any());
    }
}