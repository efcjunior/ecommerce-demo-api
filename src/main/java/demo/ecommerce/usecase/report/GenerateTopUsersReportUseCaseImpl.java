package demo.ecommerce.usecase.report;

import demo.ecommerce.entity.UserPurchaseSummary;
import demo.ecommerce.gateway.ReportRepositoryGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateTopUsersReportUseCaseImpl implements GenerateTopUsersReportUseCase {

    private final ReportRepositoryGateway reportRepository;

    public GenerateTopUsersReportUseCaseImpl(ReportRepositoryGateway reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public TopUsersReportOutput execute(TopUsersReportInput input) {
        List<UserPurchaseSummary> users = reportRepository.findTopUsers(
                input.startDate(),
                input.endDate()
        );
        return new TopUsersReportOutput(users);
    }
}