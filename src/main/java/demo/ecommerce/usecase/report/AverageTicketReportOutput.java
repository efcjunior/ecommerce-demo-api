package demo.ecommerce.usecase.report;

import demo.ecommerce.entity.UserAverageTicket;

import java.util.List;

public record AverageTicketReportOutput(List<UserAverageTicket> users) {}