package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.repo.CustomerPurchasesRepo;
import com.crm.deshkarStudio.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SalesReportServiceImpl {

    private final CustomerPurchasesRepo purchaseRepository;
    private final EmailService emailService;

    public void sendDailySalesReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        // Fetch today's purchases
        List<CustomerPurchases> todaysPurchases = purchaseRepository.findTodaysPurchases(startOfDay, endOfDay);

        if (todaysPurchases.isEmpty()) {
            emailService.sendEmail("itschinmayd@gmail.com",
                    "Daily Sales Report - " + today,
                    "No sales today.");
            return;
        }

        // Total revenue and balance
        double totalRevenue = todaysPurchases.stream()
                .mapToDouble(CustomerPurchases::getAdvancePaid)
                .sum();

        double totalBalance = todaysPurchases.stream()
                .mapToDouble(CustomerPurchases::getBalance)
                .sum();

        // Customers visited today (distinct)
        long customersVisited = todaysPurchases.stream()
                .map(p -> p.getCustomer().getId())
                .distinct()
                .count();

        // Number of Transactions
        long totalTransactions = todaysPurchases.size();

        // Number of Sales Lodged
        long salesLodged = todaysPurchases.size();

        // Payment summary grouping
        Map<String, List<CustomerPurchases>> paymentGrouped = todaysPurchases.stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentMethod().toUpperCase()));

        StringBuilder paymentDetails = new StringBuilder();
        for (String method : List.of("CASH", "CARD", "UPI")) {
            List<CustomerPurchases> list = paymentGrouped.getOrDefault(method, List.of());
            double amount = list.stream().mapToDouble(CustomerPurchases::getAdvancePaid).sum();
            paymentDetails.append("<tr>")
                    .append("<td>").append(method).append("</td>")
                    .append("<td>").append(list.size()).append("</td>")
                    .append("<td>").append("₹").append(String.format("%.2f", amount)).append("</td>")
                    .append("</tr>");
        }

        // HTML Template
        String htmlTemplate = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
        <meta charset="UTF-8">
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #eef1f4;
                margin: 0;
                padding: 0;
            }
            .header {
                background-color: #1a237e;
                color: #fff;
                text-align: center;
                padding: 14px 0;
                font-size: 20px;
                font-weight: bold;
            }
            .container {
                max-width: 650px;
                background: #fff;
                margin: 25px auto;
                padding: 25px;
                border-radius: 10px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .stat-box {
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
                margin-top: 15px;
            }
            .stat {
                background:#f7f9fc;
                border-radius: 10px;
                padding: 15px;
                width: 48%%;
                margin-bottom: 10px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            }
            .stat p {
                margin: 5px 0;
                font-size: 14px;
                color: #444;
            }
            .label {
                font-weight: 600;
                color:#1a237e;
            }
            table {
                width: 100%%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th {
                background-color: #1a237e;
                color: #fff;
                padding: 10px;
            }
            td {
                border: 1px solid #ddd;
                padding: 10px;
                font-size: 14px;
            }
            tr:nth-child(even){
                background-color:#f2f4f7;
            }
        </style>
        </head>
        <body>

        <div class="header">
            Daily Sales Report - %s
        </div>

        <div class="container">
            
            <div class="stat-box">
                <div class="stat"><p class="label">Total Revenue:</p> <p>₹%.2f</p></div>
                <div class="stat"><p class="label">Total Balance:</p> <p>₹%.2f</p></div>
                <div class="stat"><p class="label">Customers Visited:</p> <p>%d</p></div>
                <div class="stat"><p class="label">Total Transactions:</p> <p>%d</p></div>
                <div class="stat"><p class="label">Sales Lodged:</p> <p>%d</p></div>
            </div>

            <h3 style="color:#1a237e; margin-top:25px;">Payments Summary</h3>

            <table>
                <tr>
                    <th>Payment Method</th>
                    <th>Count</th>
                    <th>Amount</th>
                </tr>
                %s
            </table>

        </div>
        </body>
        </html>
        """;


        String htmlContent = String.format(htmlTemplate,
                today,
                totalRevenue,
                totalBalance,
                customersVisited,
                totalTransactions,
                salesLodged,
                paymentDetails.toString()
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        log.info("Sending mail with Daily Sales Report");
        emailService.sendEmail("itschinmayd@gmail.com",
                "Daily Sales Report " + formattedDate,
                htmlContent);

        log.info("Email sent successfully");
    }

}
