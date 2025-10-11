package demo.ecommerce.usecases.product.command.updateproduct;

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
class UpdateProductInteractorTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private UpdateProductInteractor interactor;

    @Test
    void shouldUpdateProductWhenInputIsValid() {
        // given
        Product existing = Product.create(
                "Old Name",
                "Old Description",
                new BigDecimal("100.00"),
                "Category",
                5
        );

        when(repository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var input = new UpdateProductInput(
                existing.getId(),
                "New Name",
                "Updated Description",
                new BigDecimal("120.00"),
                "Updated Category",
                8
        );

        // when
        var output = interactor.execute(input);

        // then
        verify(repository).findById(existing.getId());
        verify(repository).save(existing);

        assertEquals(existing.getId(), output.productId());
        assertNotNull(output.updatedAt());
        assertEquals("New Name", existing.getName());
        assertEquals("Updated Description", existing.getDescription());
        assertEquals(0, existing.getPrice().compareTo(new BigDecimal("120.00")));
        assertEquals("Updated Category", existing.getCategory());
        assertEquals(8, existing.getStockQuantity());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        var input = new UpdateProductInput(
                id,
                "Any",
                "Any",
                new BigDecimal("50.00"),
                "Any",
                5
        );

        assertThrows(NotFoundException.class, () -> interactor.execute(input));
        verify(repository, never()).save(any());
    }
}
