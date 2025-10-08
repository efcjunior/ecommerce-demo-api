package demo.ecommerce.usecase.report;

import demo.ecommerce.entity.UserPurchaseSummary;

import java.util.List;

public record TopUsersReportOutput(List<UserPurchaseSummary> users) {}