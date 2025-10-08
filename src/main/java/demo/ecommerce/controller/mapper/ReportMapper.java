package demo.ecommerce.controller.mapper;

import demo.ecommerce.controller.dto.AverageTicketReportResponse;
import demo.ecommerce.controller.dto.MonthlyRevenueReportResponse;
import demo.ecommerce.controller.dto.ReportDateRangeRequest;
import demo.ecommerce.controller.dto.TopUsersReportResponse;
import demo.ecommerce.entity.UserAverageTicket;
import demo.ecommerce.entity.UserPurchaseSummary;
import demo.ecommerce.usecase.report.AverageTicketReportInput;
import demo.ecommerce.usecase.report.MonthlyRevenueReportOutput;
import demo.ecommerce.usecase.report.TopUsersReportInput;

import java.util.List;

public class ReportMapper {

    public static TopUsersReportInput toInput(ReportDateRangeRequest request) {
        return new TopUsersReportInput(request.startDate(), request.endDate());
    }

    public static AverageTicketReportInput toAverageInput(ReportDateRangeRequest request) {
        return new AverageTicketReportInput(request.startDate(), request.endDate());
    }

    public static List<TopUsersReportResponse> toTopUsersResponse(List<UserPurchaseSummary> users) {
        return users.stream()
                .map(u -> new TopUsersReportResponse(u.getUserId(), u.getTotalSpent()))
                .toList();
    }

    public static List<AverageTicketReportResponse> toAverageTicketResponse(List<UserAverageTicket> users) {
        return users.stream()
                .map(u -> new AverageTicketReportResponse(u.getUserId(), u.getAverageTicket()))
                .toList();
    }

    public static MonthlyRevenueReportResponse toMonthlyResponse(MonthlyRevenueReportOutput output) {
        return new MonthlyRevenueReportResponse(output.totalRevenue());
    }
}
