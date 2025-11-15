package com.crm.deshkarStudio.scheduler;

import com.crm.deshkarStudio.services.impl.SalesReportServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportScheduler {
    private final SalesReportServiceImpl reportService;

    // Schedule every day at 8 AM
    @Scheduled(cron = "0 20 2 * * ?")
    public void sendDailyReport() {
        reportService.sendDailySalesReport();
    }
}
