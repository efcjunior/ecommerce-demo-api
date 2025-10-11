package demo.ecommerce.usecases.product.command.createproduct;

import demo.ecommerce.entities.product.Product;
import demo.ecommerce.usecases.ports.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductInteractorTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private CreateProductInteractor interactor;

    @Test
    void shouldCreateProductWhenInputIsValid() {
        var input = new CreateProductInput(
                "Gaming Laptop",
                "RTX 4060, 32GB RAM",
                new BigDecimal("9999.99"),
                "Electronics",
                10
        );

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var output = interactor.execute(input);

        verify(repository).save(productCaptor.capture());
        Product saved = productCaptor.getValue();

        assertNotNull(output);
        assertNotNull(saved.getId());
        assertEquals("Gaming Laptop", saved.getName());
        assertEquals(output.productId(), saved.getId());
        assertNotNull(output.createdAt());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        var input = new CreateProductInput(
                "", // invalid name
                "Description",
                new BigDecimal("100.00"),
                "Category",
                5
        );

        assertThrows(RuntimeException.class, () -> interactor.execute(input));
        verify(repository, never()).save(any());
    }
}
