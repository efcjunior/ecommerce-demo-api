package demo.ecommerce.controller.dto;

import java.math.BigDecimal;

public record AverageTicketReportResponse(
        String userId,
        BigDecimal averageTicket
) {}