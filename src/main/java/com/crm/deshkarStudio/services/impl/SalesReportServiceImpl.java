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
            emailService.sendEmail("itschinmayd@gmail.com", "Daily Sales Report - " + today, "No sales today.");
            return;
        }

        // Total income and balance
        double totalIncome = todaysPurchases.stream().mapToDouble(CustomerPurchases::getAdvancePaid).sum();
        double totalBalance = todaysPurchases.stream().mapToDouble(CustomerPurchases::getBalance).sum();

        // Customers visited today
        long customersVisited = todaysPurchases.stream().map(CustomerPurchases::getCustomer).distinct().count();

        // Pending tasks
        List<CustomerPurchases> pendingTasks = todaysPurchases.stream()
                .filter(p -> !"COMPLETED".equalsIgnoreCase(p.getOrderStatus()) || !"PAID".equalsIgnoreCase(p.getPaymentStatus()))
                .toList();
        String pendingHtml = pendingTasks.stream()
                .map(p -> "<li>PurchaseID: " + p.getPurchaseId() + ", Customer: " + p.getCustomer().getCustomerName() +
                        ", Status: " + p.getOrderStatus() + ", Payment: " + p.getPaymentStatus() + "</li>")
                .collect(Collectors.joining("\n"));

        // Payment summary
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
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                body { font-family: Arial, sans-serif; margin:0; padding:0; background-color:#f4f6f8; }
                .container { max-width:600px; margin:20px auto; background:#fff; padding:20px; border-radius:8px; box-shadow:0 0 10px rgba(0,0,0,0.1); }
                h2 { color:#333; }
                table { width:100%%; border-collapse:collapse; margin-bottom:20px; }
                th, td { border:1px solid #ddd; padding:10px; text-align:left; }
                th { background-color:#f4f4f4; }
                ul { padding-left:20px; }
                @media only screen and (max-width:600px) {
                  .container { padding:15px; }
                  th, td { padding:8px; }
                }
                </style>
                </head>
                <body>
                <div class="container">
                <h2>Daily Sales Report - %s</h2>
                <p><strong>Total Income:</strong> ₹%.2f</p>
                <p><strong>Total Balance:</strong> ₹%.2f</p>
                <p><strong>Customers Visited:</strong> %d</p>

                <h3>Pending Tasks (%d)</h3>
                <ul>
                %s
                </ul>

                <h3>Payments Summary</h3>
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
                totalIncome,
                totalBalance,
                customersVisited,
                pendingTasks.size(),
                pendingHtml,
                paymentDetails.toString()
        );
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(dateTimeFormatter);
        log.info("Sending mail with Daily Sales Report");
        emailService.sendEmail("itschinmayd@gmail.com", "Testing report" +formattedDate, htmlContent);

        log.info("Email sent successfully");
    }
}
