package demo.ecommerce.usecase.report;

import demo.ecommerce.gateway.ReportRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerateMonthlyRevenueReportUseCaseTest {

    private ReportRepositoryGateway reportRepository;
    private GenerateMonthlyRevenueReportUseCase useCase;

    @BeforeEach
    void setUp() {
        reportRepository = mock(ReportRepositoryGateway.class);
        useCase = new GenerateMonthlyRevenueReportUseCaseImpl(reportRepository);
    }

    @Test
    void shouldReturnTotalRevenueForMonthAndYear() {
        MonthlyRevenueReportInput input = new MonthlyRevenueReportInput(10, 2025);
        when(reportRepository.findMonthlyRevenue(10, 2025)).thenReturn(BigDecimal.valueOf(150000));

        MonthlyRevenueReportOutput output = useCase.execute(input);

        assertNotNull(output);
        assertEquals(BigDecimal.valueOf(150000), output.totalRevenue());
        verify(reportRepository).findMonthlyRevenue(10, 2025);
    }

    @Test
    void shouldReturnZeroWhenNoRevenueFound() {
        MonthlyRevenueReportInput input = new MonthlyRevenueReportInput(5, 2025);
        when(reportRepository.findMonthlyRevenue(5, 2025)).thenReturn(BigDecimal.ZERO);

        MonthlyRevenueReportOutput output = useCase.execute(input);

        assertEquals(BigDecimal.ZERO, output.totalRevenue());
    }
}