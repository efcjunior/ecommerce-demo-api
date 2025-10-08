package demo.ecommerce.usecase.report;

import demo.ecommerce.entity.UserAverageTicket;
import demo.ecommerce.gateway.ReportRepositoryGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateAverageTicketReportUseCaseImpl implements GenerateAverageTicketReportUseCase {

    private final ReportRepositoryGateway reportRepository;

    public GenerateAverageTicketReportUseCaseImpl(ReportRepositoryGateway reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public AverageTicketReportOutput execute(AverageTicketReportInput input) {
        List<UserAverageTicket> users = reportRepository.findAverageTicket(
                input.startDate(),
                input.endDate()
        );
        return new AverageTicketReportOutput(users);
    }
}