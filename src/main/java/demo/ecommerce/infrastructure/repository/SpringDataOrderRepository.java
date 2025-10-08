package demo.ecommerce.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
