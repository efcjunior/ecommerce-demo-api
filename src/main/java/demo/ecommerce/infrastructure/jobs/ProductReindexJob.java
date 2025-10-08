package demo.ecommerce.infrastructure.jobs;

import demo.ecommerce.gateway.ProductRepositoryGateway;
import demo.ecommerce.gateway.ProductSearchGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductReindexJob {

    private final ProductRepositoryGateway productRepository;
    private final ProductSearchGateway productSearch;

    public ProductReindexJob(ProductRepositoryGateway productRepository,
                             ProductSearchGateway productSearch) {
        this.productRepository = productRepository;
        this.productSearch = productSearch;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void reindexProducts() {
        log.info("🔄 Starting product reindexing in Elasticsearch...");
        var products = productRepository.findAll();

        int count = 0;
        for (var product : products) {
            try {
                productSearch.index(product);
                count++;
            } catch (Exception e) {
                log.error("❌ Failed to reindex product {}: {}", product.getId(), e.getMessage());
            }
        }

        log.info("✅ Reindexing completed: {} products successfully reindexed.", count);
    }
}
