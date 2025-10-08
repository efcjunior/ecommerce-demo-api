package demo.ecommerce.usecase.report;

import demo.ecommerce.gateway.ReportRepositoryGateway;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GenerateMonthlyRevenueReportUseCaseImpl implements GenerateMonthlyRevenueReportUseCase {

    private final ReportRepositoryGateway reportRepository;

    public GenerateMonthlyRevenueReportUseCaseImpl(ReportRepositoryGateway reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public MonthlyRevenueReportOutput execute(MonthlyRevenueReportInput input) {
        BigDecimal total = reportRepository.findMonthlyRevenue(input.month(), input.year());
        return new MonthlyRevenueReportOutput(total);
    }
}