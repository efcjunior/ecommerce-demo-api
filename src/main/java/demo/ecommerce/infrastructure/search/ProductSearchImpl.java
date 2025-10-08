package demo.ecommerce.infrastructure.search;

import demo.ecommerce.entity.Product;
import demo.ecommerce.gateway.ProductSearchGateway;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProductSearchImpl implements ProductSearchGateway {

    private final ElasticsearchOperations elasticsearchOperations;
    private final SpringDataProductSearchRepository repository;

    public ProductSearchImpl(ElasticsearchOperations elasticsearchOperations,
                             SpringDataProductSearchRepository repository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.repository = repository;
    }

    @Override
    public void index(Product product) {
        repository.save(ProductDocument.fromDomain(product));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Product> search(String name, String category, Double minPrice, Double maxPrice) {

        // Busca fuzzy por nome
        if (name != null && !name.isBlank()) {
            NativeQuery query = NativeQuery.builder()
                    .withQuery(q -> q.match(m -> m
                            .field("name")
                            .query(name)
                            .fuzziness("AUTO")
                    ))
                    .build();

            return elasticsearchOperations.search(query, ProductDocument.class)
                    .stream()
                    .map(SearchHit::getContent)
                    .filter(p -> (category == null || p.getCategory().equalsIgnoreCase(category)))
                    .filter(p -> (minPrice == null || p.getPrice().compareTo(BigDecimal.valueOf(minPrice)) >= 0))
                    .filter(p -> (maxPrice == null || p.getPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0))
                    .filter(p -> p.getStockQuantity() > 0)
                    .map(ProductDocument::toDomain)
                    .collect(Collectors.toList());
        }

        // Filtros simples (sem nome)
        Criteria criteria = new Criteria("stockQuantity").greaterThan(0);

        if (category != null && !category.isBlank()) {
            criteria = criteria.and(new Criteria("category").is(category));
        }

        if (minPrice != null && maxPrice != null) {
            criteria = criteria.and(new Criteria("price").between(minPrice, maxPrice));
        } else if (minPrice != null) {
            criteria = criteria.and(new Criteria("price").greaterThanEqual(minPrice));
        } else if (maxPrice != null) {
            criteria = criteria.and(new Criteria("price").lessThanEqual(maxPrice));
        }

        CriteriaQuery query = new CriteriaQuery(criteria);

        return elasticsearchOperations.search(query, ProductDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .map(ProductDocument::toDomain)
                .collect(Collectors.toList());
    }
}
