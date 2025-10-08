package demo.ecommerce.controller.dto;

import java.math.BigDecimal;

public record TopUsersReportResponse(
        String userId,
        BigDecimal totalSpent
) {}