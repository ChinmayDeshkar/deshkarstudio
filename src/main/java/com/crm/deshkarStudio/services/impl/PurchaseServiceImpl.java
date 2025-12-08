package com.crm.deshkarStudio.services.impl;

import com.crm.deshkarStudio.dto.PurchaseDetailsDTO;
import com.crm.deshkarStudio.dto.PurchaseUpdateRequest;
import com.crm.deshkarStudio.dto.TaskDTO;
import com.crm.deshkarStudio.model.*;
import com.crm.deshkarStudio.repo.CustomerPurchasesRepo;
import com.crm.deshkarStudio.repo.CustomerRepo;
import com.crm.deshkarStudio.services.InvoiceService;
import com.crm.deshkarStudio.services.NoteService;
import com.crm.deshkarStudio.services.PaymentService;
import com.crm.deshkarStudio.services.PurchaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final CustomerRepo customerRepo;
    private final CustomerPurchasesRepo purchaseRepo;
    private final NoteService noteService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;


    @Override
    public ResponseEntity<?> addPurchase(CustomerPurchases purchase) {
        Customer payloadCustomer = purchase.getCustomer();

        log.info("Adding new Purchase : " + purchase);

        // 1. Check if customer exists
        Customer customer = customerRepo.findByPhoneNumber(payloadCustomer.getPhoneNumber())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setCustomerName(payloadCustomer.getCustomerName());
                    newCustomer.setEmail(payloadCustomer.getEmail());
                    newCustomer.setPhoneNumber(payloadCustomer.getPhoneNumber());
                    newCustomer.setAddress(payloadCustomer.getAddress());
                    newCustomer.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
                    return customerRepo.save(newCustomer);
                });

        log.info("Customer: " + customer);
        purchase.setCustomer(customer);

        // 2. Calculate balance & payment status
        if (purchase.getAdvancePaid() < purchase.getPrice()) {
            double balance = purchase.getPrice() - purchase.getAdvancePaid();
            purchase.setBalance(balance);

            if (balance > 0)
                purchase.setPaymentStatus("PENDING");
        }

        purchase.setOrderStatus("CREATED");
        log.info("Purchase received: " + purchase);
        log.info(("Items: " + purchase.getItems()));

        // 3. Process items (IMPORTANT)
        if (purchase.getItems() != null) {

            double total = 0;

            for (PurchaseItems item : purchase.getItems()) {
                // Set purchase reference
                item.setPurchase(purchase);

                // calculate total for this item
                item.setTotal(item.getQuantity() * item.getItemPrice());

                total += item.getTotal();
            }

            // override purchase price (optional)
            purchase.setPrice(total);
        }

        // 4. Save purchase along with items (cascade=ALL does the magic)
        CustomerPurchases savedPurchase = purchaseRepo.save(purchase);
        customer.setPurchaseCount(customer.getPurchaseCount() + 1);
        customerRepo.save(customer);
        log.info("Purchased added. Now adding in history");

        // 5. Send to history
        noteService.addUpdateNote(savedPurchase, "Job created");

        paymentService.addPayment(purchase.getPurchaseId(), purchase.getAdvancePaid(), purchase.getPaymentMethod());

        return ResponseEntity.ok(
                Map.of("message", "Purchase added successfully",
                        "customerId", customer.getId(),
                        "purchaseId", savedPurchase.getPurchaseId())
        );
    }

    @Override
    public PurchaseDetailsDTO getPurchaseById(long id) {

        log.debug("Getting Purchase details for id = " + id);
        CustomerPurchases purchase = purchaseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Error while getting purchase with id, " + id));

        PurchaseDetailsDTO purchaseDetailsDTO = new PurchaseDetailsDTO(
                purchase.getPurchaseId(),
                purchase.getCustomer(),
                purchase.getPrice(),
                purchase.getPaymentMethod(),
                purchase.getPaymentStatus(),
                purchase.getOrderStatus(),
                purchase.getAdvancePaid(),
                purchase.getBalance(),
                purchase.getCreatedDate(),
                purchase.getUpdatedDate(),
                purchase.getUpdatedBy(),
                purchase.getRemarks(),
                purchase.getItems()
        );

        return purchaseDetailsDTO;
    }

    @Override
    public List<TaskDTO> getPurchaseByCustId(long id) {

        List<CustomerPurchases> purchases = purchaseRepo.findByCustomerId(id);
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(CustomerPurchases purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getPurchaseId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            tasks.add(task);
        }

        return tasks;
    }

    @Override
    public List<TaskDTO> getPurchaseByCustName(String name) {
        List<CustomerPurchases> purchases = purchaseRepo.findByCustomerName(name);
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(CustomerPurchases purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getPurchaseId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            tasks.add(task);
        }

        return tasks;
    }

    @Override
    public List<TaskDTO> getPurchaseByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepo.findByPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("Purchase not found with phone number " + phoneNumber));
        List<TaskDTO> tasks = getPurchaseByCustId(customer.getId());
        return tasks;
    }

    private List<TaskDTO> mapToDTO(List<CustomerPurchases> purchases) {
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(CustomerPurchases purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getPurchaseId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public List<TaskDTO> getTodayPurchases() {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        log.info("Fetching purchases between {} and {}", startOfDay, endOfDay);
        List<CustomerPurchases> purchases = purchaseRepo.findByCreatedDateBetween(startOfDay, endOfDay);

        return mapToDTO(purchases);
    }

    @Override
    public List<TaskDTO> getPurchasesThisMonth() {
        List<CustomerPurchases> purchases = purchaseRepo.findPurchasesThisMonth();
        return mapToDTO(purchases);
    }

    @Override
    public List<TaskDTO> getPurchasesByRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching purchases from {} to {}", startDate, endDate);
        List<CustomerPurchases> purchases = purchaseRepo.findByCreatedDateBetween(startDate, endDate);
        return mapToDTO(purchases);
    }


    @Override
    public List<TaskDTO> getPendingTasks() {
        List<CustomerPurchases> purchases = purchaseRepo.findPendingTasks();
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(CustomerPurchases purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getPurchaseId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            task.setDte_updated(purchase.getUpdatedDate());
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public List<TaskDTO> getRecentTasks() {
            List<String> statuses = List.of("COMPLETED", "DELIVERED", "CANCELLED");
        LocalDateTime fromDate = LocalDateTime.now(ZoneId.of("Asia/Kolkata")).minusDays(2);

        List<CustomerPurchases> purchases = purchaseRepo.findRecentCompletedOrders(statuses, fromDate);
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(CustomerPurchases purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getPurchaseId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            task.setDte_updated(purchase.getUpdatedDate());
            tasks.add(task);
        }
        return tasks;
//        return purchaseRepo.findRecentCompletedOrders(statuses, fromDate);
    }

    @Override
    public CustomerPurchases updatePurchase(long purchaseId, PurchaseUpdateRequest newPurchase) {

        // Load existing purchase (managed entity)
        CustomerPurchases oldPurchase = purchaseRepo.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("PurchaseId not found: " + purchaseId));

        List<String> notes = addNotes(newPurchase, oldPurchase);

        if (newPurchase.isCustomerUpdated()) {
            Customer existingCustomer = oldPurchase.getCustomer();  // managed entity

            Customer updatedData = newPurchase.getCustomer();

            existingCustomer.setCustomerName(updatedData.getCustomerName());
            existingCustomer.setPhoneNumber(updatedData.getPhoneNumber());
            existingCustomer.setAddress(updatedData.getAddress());
            existingCustomer.setEmail(updatedData.getEmail());
        }

        double amount = newPurchase.getAdvancePaid() - oldPurchase.getAdvancePaid();
        // Update simple fields
        oldPurchase.setPrice(newPurchase.getPrice());
        oldPurchase.setPaymentMethod(newPurchase.getPaymentMethod());
        oldPurchase.setPaymentStatus(newPurchase.getPaymentStatus());
        oldPurchase.setOrderStatus(newPurchase.getOrderStatus());
        oldPurchase.setAdvancePaid(newPurchase.getAdvancePaid());
        oldPurchase.setBalance(newPurchase.getBalance());
        oldPurchase.setRemarks(newPurchase.getRemarks());
        oldPurchase.setUpdatedBy(newPurchase.getUpdatedBy());
        oldPurchase.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));

        oldPurchase.getItems().clear();

        for (PurchaseItems item : newPurchase.getItems()) {
            item.setItemId(null);
            item.setPurchase(oldPurchase);
            oldPurchase.getItems().add(item);
        }

        CustomerPurchases saved = purchaseRepo.save(oldPurchase);
        if(amount > 0)
            paymentService.addPayment(saved.getPurchaseId(), amount, saved.getPaymentMethod());


        // Save notes
        for (String n : notes) {
            noteService.addUpdateNote(saved, n);
        }

        // If OrderStatus == READY -> generate invoice
        if (newPurchase.getOrderStatus().equals("READY")) {
            try {
                invoiceService.generateInvoice(newPurchase.getPurchaseId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return saved;
    }

    List<String> addNotes(PurchaseUpdateRequest newPurchase, CustomerPurchases oldPurchase){
        List<String> notes = new ArrayList<>();

        // --- CHECK CHANGES ---
        if (!Objects.equals(oldPurchase.getOrderStatus(), newPurchase.getOrderStatus())) {

            notes.add("Order status updated | "
                    + oldPurchase.getOrderStatus() + " → " + newPurchase.getOrderStatus());
        }

        if (!Objects.equals(oldPurchase.getPaymentStatus(), newPurchase.getPaymentStatus())) {
            notes.add("Payment status updated | "
                    + oldPurchase.getPaymentStatus() + " → " + newPurchase.getPaymentStatus());
        }

        if (!Objects.equals(oldPurchase.getAdvancePaid(), newPurchase.getAdvancePaid())) {
            notes.add("Advance updated | "
                    + oldPurchase.getAdvancePaid() + " → " + newPurchase.getAdvancePaid());
        }

        if (!Objects.equals(oldPurchase.getRemarks(), newPurchase.getRemarks())) {
            notes.add("Remarks updated");
        }

        return notes;
    }

    Payment addPayment(long purchaseId, double amount, String paymentType){
        return null;
    }

}
