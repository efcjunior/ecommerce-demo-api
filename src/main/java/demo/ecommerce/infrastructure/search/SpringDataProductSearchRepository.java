package demo.ecommerce.infrastructure.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataProductSearchRepository extends ElasticsearchRepository<ProductDocument, UUID> {
    List<ProductDocument> findByNameContainingIgnoreCase(String name);
    List<ProductDocument> findByCategoryIgnoreCase(String category);
}
