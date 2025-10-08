package demo.ecommerce.controller;

import demo.ecommerce.controller.dto.AverageTicketReportResponse;
import demo.ecommerce.controller.dto.MonthlyRevenueReportResponse;
import demo.ecommerce.controller.dto.TopUsersReportResponse;
import demo.ecommerce.controller.mapper.ReportMapper;
import demo.ecommerce.usecase.report.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final GenerateTopUsersReportUseCase topUsersUseCase;
    private final GenerateAverageTicketReportUseCase averageTicketUseCase;
    private final GenerateMonthlyRevenueReportUseCase monthlyRevenueUseCase;

    public ReportController(GenerateTopUsersReportUseCase topUsersUseCase,
                            GenerateAverageTicketReportUseCase averageTicketUseCase,
                            GenerateMonthlyRevenueReportUseCase monthlyRevenueUseCase) {
        this.topUsersUseCase = topUsersUseCase;
        this.averageTicketUseCase = averageTicketUseCase;
        this.monthlyRevenueUseCase = monthlyRevenueUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-users")
    public ResponseEntity<List<TopUsersReportResponse>> topUsers(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        var input = new TopUsersReportInput(
                startDate != null ? java.time.LocalDate.parse(startDate) : java.time.LocalDate.now().minusDays(30),
                endDate != null ? java.time.LocalDate.parse(endDate) : java.time.LocalDate.now()
        );

        var output = topUsersUseCase.execute(input);
        var response = ReportMapper.toTopUsersResponse(output.users());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/average-ticket")
    public ResponseEntity<List<AverageTicketReportResponse>> averageTicket(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        var input = new AverageTicketReportInput(
                startDate != null ? java.time.LocalDate.parse(startDate) : java.time.LocalDate.now().minusDays(30),
                endDate != null ? java.time.LocalDate.parse(endDate) : java.time.LocalDate.now()
        );

        var output = averageTicketUseCase.execute(input);
        var response = ReportMapper.toAverageTicketResponse(output.users());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly-revenue")
    public ResponseEntity<MonthlyRevenueReportResponse> monthlyRevenue(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        var now = java.time.YearMonth.now();

        int targetMonth = (month != null) ? month : now.getMonthValue();
        int targetYear  = (year  != null) ? year  : now.getYear();

        var input = new MonthlyRevenueReportInput(targetMonth, targetYear);
        var output = monthlyRevenueUseCase.execute(input);
        var response = ReportMapper.toMonthlyResponse(output);

        return ResponseEntity.ok(response);
    }
}
