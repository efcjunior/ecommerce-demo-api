package demo.ecommerce.controller.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public record ReportDateRangeRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {}