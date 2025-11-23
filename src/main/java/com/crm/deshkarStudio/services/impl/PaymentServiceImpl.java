package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.Payment;
import com.crm.deshkarStudio.repo.PaymentRepo;
import com.crm.deshkarStudio.services.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;

    @Override
    public Payment addPayment(long purchaseId, double amount, String paymentType) {
        log.debug("Adding Payment");
        Payment payment = new Payment();

        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(amount);
        payment.setPaymentType(paymentType);
        payment.setPurchaseId(purchaseId);

        return paymentRepo.save(payment);
    }


    // ====== Daily Income ======
    public Double getTodayIncome() {
        return paymentRepo.getTodayIncome() != null ? paymentRepo.getTodayIncome() : 0.0;
    }

    // ====== Weekly Income ======
    public Double getWeeklyIncome() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        Double income = paymentRepo.getWeeklyIncome(start, end);
        return income != null ? income : 0.0;
    }

    // ====== Monthly Income ======
    public Double getMonthlyIncome() {
        return paymentRepo.getMonthlyIncome() != null ? paymentRepo.getMonthlyIncome() : 0.0;
    }

    // ====== Income in Custom Range ======
    public Double getIncomeInRange(LocalDate start, LocalDate end) {
        return paymentRepo.getIncomeInRange(start, end) != null ? paymentRepo.getIncomeInRange(start, end) : 0.0;
    }

    public List<Payment> getPaymentsInRange(LocalDate start, LocalDate end) {
        return paymentRepo.findByPaymentDateBetween(start, end);
    }

    // ====== Last 7 Days Daily Income ======
    public Map<LocalDate, Double> getLast7DaysDailyIncome() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6); // last 7 days including today

        List<Object[]> results = paymentRepo.getLast7DaysIncome(startDate);

        Map<LocalDate, Double> dailyIncome = new LinkedHashMap<>();
        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            Double total = (Double) row[1];
            dailyIncome.put(date, total);
        }

        // Fill missing days with 0
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            dailyIncome.putIfAbsent(date, 0.0);
        }

        return new TreeMap<>(dailyIncome);
    }
}
