package demo.ecommerce.usecases.product.command.deleteproduct;

import demo.ecommerce.entities.product.Product;
import demo.ecommerce.usecases.common.exceptions.NotFoundException;
import demo.ecommerce.usecases.ports.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductInteractorTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private DeleteProductInteractor interactor;

    @Test
    void shouldDeleteProductWhenItExists() {
        Product existing = Product.create(
                "Keyboard",
                "Mechanical RGB keyboard",
                new BigDecimal("299.99"),
                "Peripherals",
                15
        );

        when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));
        doNothing().when(repository).delete(existing.getId());

        var input = new DeleteProductInput(existing.getId());
        var output = interactor.execute(input);

        verify(repository).findById(existing.getId());
        verify(repository).delete(existing.getId());

        assertTrue(output.success());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        var input = new DeleteProductInput(id);

        assertThrows(NotFoundException.class, () -> interactor.execute(input));

        verify(repository, never()).delete(any());
    }
}
