package com.crm.deshkarStudio.repo;

import com.crm.deshkarStudio.dto.RevenueDTO;
import com.crm.deshkarStudio.model.CustomerPurchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerPurchasesRepo extends JpaRepository<CustomerPurchases, Long> {
    List<CustomerPurchases> findByCreatedDate(LocalDateTime date);

    List<CustomerPurchases> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p FROM CustomerPurchases p WHERE p.createdDate BETWEEN :start AND :end")
    List<CustomerPurchases> findByDateRange(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p FROM CustomerPurchases p WHERE MONTH(p.createdDate) = MONTH(CURRENT_DATE) AND YEAR(p.createdDate) = YEAR(CURRENT_DATE)")
    List<CustomerPurchases> findPurchasesThisMonth();

    // Revenues

    // 📅 Daily revenue (last 7 or 30 days)
    @Query("SELECT FUNCTION('DATE', c.createdDate) AS date, SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM CustomerPurchases c GROUP BY FUNCTION('DATE', c.createdDate) ORDER BY date DESC")
    List<Object[]> getRevenuePerDay();

    // 📆 Monthly revenue
    @Query("SELECT FUNCTION('MONTH', c.createdDate) AS month, FUNCTION('YEAR', c.createdDate) AS year, " +
            "SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM CustomerPurchases c GROUP BY year, month ORDER BY year DESC, month DESC")
    List<Object[]> getRevenuePerMonth();

    // 📅 Yearly revenue
    @Query("SELECT FUNCTION('YEAR', c.createdDate) AS year, SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM CustomerPurchases c GROUP BY year ORDER BY year DESC")
    List<Object[]> getRevenuePerYear();

    // 📆 Revenue in a custom range
    @Query("SELECT FUNCTION('DATE', c.createdDate) AS date, SUM(c.price), SUM(c.balance), COUNT(c) " +
            "FROM CustomerPurchases c WHERE c.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', c.createdDate) ORDER BY date ASC")
    List<Object[]> getRevenueBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 💳 Transactions by payment method
    @Query("SELECT c.paymentMethod, COUNT(c) FROM CustomerPurchases c GROUP BY c.paymentMethod")
    List<Object[]> getTransactionCountByPaymentMethod();

//    @Query("SELECT p FROM CustomerPurchases p " +
//            "WHERE p.orderStatus NOT IN ('COMPLETED', 'DELIVERED', 'CANCELLED') " +
//            "OR p.paymentStatus <> 'PAID'")
//    List<CustomerPurchases> findPendingTasks();

    @Query("SELECT c FROM CustomerPurchases c " +
            "WHERE c.orderStatus IN :statuses " +
            "AND c.createdDate >= :fromDate")
    List<CustomerPurchases> findRecentCompletedOrders(
            @Param("statuses") List<String> statuses,
            @Param("fromDate") LocalDateTime fromDate
    );

    List<CustomerPurchases> findByCustomerId(long customerId);

    @Query("SELECT c FROM CustomerPurchases c WHERE c.createdDate >= :start AND c.createdDate < :end")
    List<CustomerPurchases> findTodaysPurchases(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(c) FROM CustomerPurchases c WHERE c.createdDate >= :start AND c.createdDate < :end")
    Long countCustomersVisitedToday(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT c FROM CustomerPurchases c  WHERE c.orderStatus NOT IN ('DELIVERED', 'CANCELLED')")
    List<CustomerPurchases> findPendingTasks();
}
