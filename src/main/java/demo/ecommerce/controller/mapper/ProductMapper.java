package demo.ecommerce.controller.mapper;

import demo.ecommerce.controller.dto.CreateProductRequest;
import demo.ecommerce.controller.dto.ProductResponse;
import demo.ecommerce.controller.dto.UpdateProductRequest;
import demo.ecommerce.entity.Product;
import demo.ecommerce.usecase.product.CreateProductInput;
import demo.ecommerce.usecase.product.UpdateProductInput;

import java.util.List;

public class ProductMapper {

    public static CreateProductInput toInput(CreateProductRequest request) {
        return new CreateProductInput(
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                request.stockQuantity()
        );
    }

    public static UpdateProductInput toInput(UpdateProductRequest request) {
        return new UpdateProductInput(
                request.productId(),
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                request.stockQuantity()
        );
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public static List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream().map(ProductMapper::toResponse).toList();
    }
}
