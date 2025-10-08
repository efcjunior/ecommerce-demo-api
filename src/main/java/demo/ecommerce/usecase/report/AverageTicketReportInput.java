package demo.ecommerce.usecase.report;

import java.time.LocalDate;

public record AverageTicketReportInput(
        LocalDate startDate,
        LocalDate endDate
) {}