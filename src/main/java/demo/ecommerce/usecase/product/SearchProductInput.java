package demo.ecommerce.usecase.product;

public record SearchProductInput(
        String name,
        String category,
        Double minPrice,
        Double maxPrice
) {}