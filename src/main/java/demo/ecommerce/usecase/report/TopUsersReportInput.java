package demo.ecommerce.usecase.report;

import java.time.LocalDate;

public record TopUsersReportInput(
        LocalDate startDate,
        LocalDate endDate
) {}