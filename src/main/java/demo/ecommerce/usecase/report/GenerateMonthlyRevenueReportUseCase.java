package demo.ecommerce.usecase.report;

public interface GenerateMonthlyRevenueReportUseCase {
    MonthlyRevenueReportOutput execute(MonthlyRevenueReportInput input);
}