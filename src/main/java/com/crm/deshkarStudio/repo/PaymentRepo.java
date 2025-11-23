package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

    @Query("SELECT FUNCTION('DATE', p.paymentDate) as date, SUM(p.amount) as total " +
            "FROM Payment p " +
            "WHERE p.paymentDate >= :startDate " +
            "GROUP BY FUNCTION('DATE', p.paymentDate) " +
            "ORDER BY FUNCTION('DATE', p.paymentDate) ASC")
    List<Object[]> getLast7DaysIncome(@Param("startDate") LocalDate startDate);


    // Daily
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate = CURRENT_DATE")
    Double getTodayIncome();

    // Weekly (last 7 days including today)
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end")
    Double getWeeklyIncome(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // Monthly (current month)
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE FUNCTION('MONTH', p.paymentDate) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', p.paymentDate) = FUNCTION('YEAR', CURRENT_DATE)")
    Double getMonthlyIncome();

    // Custom date range
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end")
    Double getIncomeInRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    List<Payment> findByPaymentDateBetween(LocalDate start, LocalDate end);
}
