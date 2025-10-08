package demo.ecommerce.controller.dto;

import java.math.BigDecimal;

public record MonthlyRevenueReportResponse(
        BigDecimal totalRevenue
) {}
