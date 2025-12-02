package com.crm.deshkarStudio.scheduler;

import com.crm.deshkarStudio.services.impl.SalesReportServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ReportScheduler {
    private final SalesReportServiceImpl reportService;

    // Schedule every day at 10 PM
    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Kolkata")
    public void sendDailyReport() {
        log.debug("Hitting Scheduled job for daily report");
        reportService.sendDailySalesReport();
    }
}
