package demo.ecommerce.usecase.report;

import demo.ecommerce.entity.UserAverageTicket;
import demo.ecommerce.gateway.ReportRepositoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerateAverageTicketReportUseCaseTest {

    private ReportRepositoryGateway reportRepository;
    private GenerateAverageTicketReportUseCase useCase;

    @BeforeEach
    void setUp() {
        reportRepository = mock(ReportRepositoryGateway.class);
        useCase = new GenerateAverageTicketReportUseCaseImpl(reportRepository);
    }

    @Test
    void shouldReturnAverageTicketPerUserWithinDateRange() {
        AverageTicketReportInput input = new AverageTicketReportInput(LocalDate.now().minusDays(30), LocalDate.now());

        List<UserAverageTicket> mockResults = List.of(
                new UserAverageTicket("user1", BigDecimal.valueOf(500)),
                new UserAverageTicket("user2", BigDecimal.valueOf(350))
        );

        when(reportRepository.findAverageTicket(input.startDate(), input.endDate()))
                .thenReturn(mockResults);

        AverageTicketReportOutput output = useCase.execute(input);

        assertEquals(2, output.users().size());
        verify(reportRepository).findAverageTicket(input.startDate(), input.endDate());
    }

    @Test
    void shouldReturnEmptyListWhenNoDataFound() {
        AverageTicketReportInput input = new AverageTicketReportInput(LocalDate.now().minusDays(30), LocalDate.now());
        when(reportRepository.findAverageTicket(any(), any())).thenReturn(List.of());

        AverageTicketReportOutput output = useCase.execute(input);

        assertTrue(output.users().isEmpty());
    }
}