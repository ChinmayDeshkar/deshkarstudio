package com.crm.deshkarStudio.services;

import com.crm.deshkarStudio.model.Invoice;

public interface InvoiceService {

    Invoice generateInvoice(Long purchaseId) throws Exception;

    byte[] downloadInvoice(Long purchaseId) throws Exception;
}
