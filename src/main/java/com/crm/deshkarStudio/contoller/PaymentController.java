package com.crm.deshkarStudio.contoller;

import com.crm.deshkarStudio.services.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/income")
public class PaymentController {

    private final PaymentService paymentService;


    // ====== Daily Income ======
    @GetMapping("/today")
    public Double getTodayIncome() {
        return paymentService.getTodayIncome();
    }

    // ====== Weekly Income ======
    @GetMapping("/weekly")
    public Double getWeeklyIncome() {
        return paymentService.getWeeklyIncome();
    }

    // ====== Monthly Income ======
    @GetMapping("/monthly")
    public Double getMonthlyIncome() {
        return paymentService.getMonthlyIncome();
    }

    // ====== Income in Custom Range ======
    @GetMapping("/range")
    public Double getIncomeInRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return paymentService.getIncomeInRange(start, end);
    }

    // ====== Last 7 Days Daily Income ======
    @GetMapping("/last7days")
    public Map<LocalDate, Double> getLast7DaysDailyIncome() {
        log.info("Coming");
        return paymentService.getLast7DaysDailyIncome();
    }
}
