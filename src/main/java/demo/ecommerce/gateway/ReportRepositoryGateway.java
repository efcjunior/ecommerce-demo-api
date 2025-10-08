package demo.ecommerce.gateway;

import demo.ecommerce.entity.UserAverageTicket;
import demo.ecommerce.entity.UserPurchaseSummary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReportRepositoryGateway {
    List<UserPurchaseSummary> findTopUsers(LocalDate startDate, LocalDate endDate);
    List<UserAverageTicket> findAverageTicket(
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    );
    BigDecimal findMonthlyRevenue(int month, int year);
}
