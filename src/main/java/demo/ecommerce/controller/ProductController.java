package demo.ecommerce.controller;

import demo.ecommerce.controller.dto.CreateProductRequest;
import demo.ecommerce.controller.dto.ProductResponse;
import demo.ecommerce.controller.dto.UpdateProductRequest;
import demo.ecommerce.controller.mapper.ProductMapper;
import demo.ecommerce.entity.Product;
import demo.ecommerce.usecase.product.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final SearchProductUseCase searchProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase,
                             UpdateProductUseCase updateProductUseCase,
                             DeleteProductUseCase deleteProductUseCase,
                             SearchProductUseCase searchProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.searchProductUseCase = searchProductUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        var input = ProductMapper.toInput(request);
        var output = createProductUseCase.execute(input);
        var product = new Product(
                output.productId(), request.name(), request.description(),
                request.price(), request.category(), request.stockQuantity(),
                output.createdAt(), output.createdAt()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toResponse(product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable("id") String id,
                                                  @Valid @RequestBody UpdateProductRequest request) {
        var input = ProductMapper.toInput(request);
        var output = updateProductUseCase.execute(input);
        var updated = new Product(
                request.productId(), request.name(), request.description(),
                request.price(), request.category(), request.stockQuantity(),
                null, output.updatedAt()
        );
        return ResponseEntity.ok(ProductMapper.toResponse(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        var input = new DeleteProductInput(java.util.UUID.fromString(id));
        deleteProductUseCase.execute(input);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        var input = new SearchProductInput(name, category, minPrice, maxPrice);
        var output = searchProductUseCase.execute(input);
        var products = ProductMapper.toResponseList(output.products());
        return ResponseEntity.ok(products);
    }
}
