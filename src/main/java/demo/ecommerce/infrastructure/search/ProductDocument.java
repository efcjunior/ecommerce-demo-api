package demo.ecommerce.infrastructure.search;

import demo.ecommerce.entity.Product;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Document(indexName = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocument {

    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private int stockQuantity;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant updatedAt;



    public static ProductDocument fromDomain(Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .updatedAt(product.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
                .build();
    }


    public Product toDomain() {
        return new Product(
                id,
                name,
                description,
                price,
                category,
                stockQuantity,
                LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()),
                LocalDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
        );
    }

}