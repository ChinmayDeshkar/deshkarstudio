package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.Payment;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    Payment addPayment(long purchaseId, double amount, String paymentType);


    // ====== Daily Income ======
    public Double getTodayIncome();

    // ====== Weekly Income ======
    public Double getWeeklyIncome();

    // ====== Monthly Income ======
    public Double getMonthlyIncome();

    // ====== Income in Custom Range ======
    public Double getIncomeInRange(LocalDate start, LocalDate end);

    public List<Payment> getPaymentsInRange(LocalDate start, LocalDate end);

    // ====== Last 7 Days Daily Income ======
    public Map<LocalDate, Double> getLast7DaysDailyIncome();
}
