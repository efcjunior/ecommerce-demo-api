package demo.ecommerce.usecase.report;

import demo.ecommerce.entity.UserPurchaseSummary;
import demo.ecommerce.gateway.ReportRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerateTopUsersReportUseCaseTest {

    private ReportRepositoryGateway reportRepository;
    private GenerateTopUsersReportUseCase useCase;

    @BeforeEach
    void setUp() {
        reportRepository = mock(ReportRepositoryGateway.class);
        useCase = new GenerateTopUsersReportUseCaseImpl(reportRepository);
    }

    @Test
    void shouldReturnTopUsersWithinDateRange() {
        TopUsersReportInput input = new TopUsersReportInput(LocalDate.now().minusDays(30), LocalDate.now());

        List<UserPurchaseSummary> mockResults = List.of(
                new UserPurchaseSummary("user1", BigDecimal.valueOf(10000)),
                new UserPurchaseSummary("user2", BigDecimal.valueOf(8500))
        );

        when(reportRepository.findTopUsers(input.startDate(), input.endDate()))
                .thenReturn(mockResults);

        TopUsersReportOutput output = useCase.execute(input);

        assertNotNull(output);
        assertEquals(2, output.users().size());
        verify(reportRepository).findTopUsers(input.startDate(), input.endDate());
    }

    @Test
    void shouldReturnEmptyListWhenNoDataFound() {
        TopUsersReportInput input = new TopUsersReportInput(LocalDate.now().minusDays(30), LocalDate.now());
        when(reportRepository.findTopUsers(any(), any())).thenReturn(List.of());

        TopUsersReportOutput output = useCase.execute(input);

        assertTrue(output.users().isEmpty());
    }
}