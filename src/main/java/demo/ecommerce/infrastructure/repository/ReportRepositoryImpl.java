package demo.ecommerce.infrastructure.repository;

import demo.ecommerce.entity.UserAverageTicket;
import demo.ecommerce.entity.UserPurchaseSummary;
import demo.ecommerce.gateway.ReportRepositoryGateway;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReportRepositoryImpl implements ReportRepositoryGateway {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserPurchaseSummary> findTopUsers(LocalDate startDate, LocalDate endDate) {
        var query = entityManager.createQuery("""
        SELECT o.userId, SUM(o.totalAmount)
        FROM OrderEntity o
        WHERE o.createdAt BETWEEN :start AND :end
        AND o.status = 'PAID'
        GROUP BY o.userId
        ORDER BY SUM(o.totalAmount) DESC
        """, Object[].class);

        query.setParameter("start", startDate.atStartOfDay());
        query.setParameter("end", endDate.plusDays(1).atStartOfDay());
        query.setMaxResults(5);

        return query.getResultList().stream()
                .map(r -> new UserPurchaseSummary(
                        r[0].toString(),
                        (BigDecimal) r[1]
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAverageTicket> findAverageTicket(LocalDate startDate, LocalDate endDate) {
        var query = entityManager.createQuery("""
            SELECT o.userId, AVG(o.totalAmount)
            FROM OrderEntity o
            WHERE o.createdAt BETWEEN :start AND :end
            GROUP BY o.userId
            """, Object[].class);

        query.setParameter("start", startDate.atStartOfDay());
        query.setParameter("end", endDate.plusDays(1).atStartOfDay());

        return query.getResultList().stream()
                .map(r -> new UserAverageTicket(
                        r[0].toString(),
                        BigDecimal.valueOf(((Number) r[1]).doubleValue())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal findMonthlyRevenue(int month, int year) {
        var start = LocalDate.of(year, month, 1).atStartOfDay();
        var end = start.plusMonths(1);

        var query = entityManager.createQuery("""
            SELECT SUM(o.totalAmount)
            FROM OrderEntity o
            WHERE o.createdAt BETWEEN :start AND :end
            AND o.status = 'PAID'
            """, BigDecimal.class);

        query.setParameter("start", start);
        query.setParameter("end", end);

        return query.getSingleResult() != null ? query.getSingleResult() : BigDecimal.ZERO;
    }
}
