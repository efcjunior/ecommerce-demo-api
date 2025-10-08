package demo.ecommerce.usecase.report;

import java.math.BigDecimal;

public record MonthlyRevenueReportOutput(
        BigDecimal totalRevenue
) {}
