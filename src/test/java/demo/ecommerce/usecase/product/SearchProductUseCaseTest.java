package demo.ecommerce.usecase.product;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchProductUseCaseTest {

    private ProductSearchGateway productSearch;
    private SearchProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productSearch = mock(ProductSearchGateway.class);
        useCase = new SearchProductUseCaseImpl(productSearch);
    }

    @Test
    void shouldReturnProductsMatchingFilters() {
        // given
        SearchProductInput input = new SearchProductInput("Phone", "Electronics", 500.0, 2000.0);

        List<Product> mockResults = List.of(
                new Product(UUID.randomUUID(), "Phone X", "Desc", BigDecimal.valueOf(1500),
                        "Electronics", 5, null, null),
                new Product(UUID.randomUUID(), "Phone Y", "Desc", BigDecimal.valueOf(1200),
                        "Electronics", 10, null, null)
        );

        when(productSearch.search("Phone", "Electronics", 500.0, 2000.0))
                .thenReturn(mockResults);

        // when
        SearchProductOutput output = useCase.execute(input);

        // then
        assertNotNull(output);
        assertEquals(2, output.products().size());
        verify(productSearch, times(1))
                .search("Phone", "Electronics", 500.0, 2000.0);
    }

    @Test
    void shouldReturnEmptyListWhenNoResultsFound() {
        SearchProductInput input = new SearchProductInput("Tablet", null, null, null);
        when(productSearch.search(any(), any(), any(), any()))
                .thenReturn(List.of());

        SearchProductOutput output = useCase.execute(input);

        assertTrue(output.products().isEmpty());
    }
}