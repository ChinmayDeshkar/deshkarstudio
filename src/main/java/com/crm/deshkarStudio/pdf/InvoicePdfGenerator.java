package com.crm.deshkarStudio.pdf;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.Invoice;
import com.crm.deshkarStudio.model.PurchaseItems;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.awt.Color;

public class InvoicePdfGenerator {

    public static byte[] generatePDF(Invoice inv, CustomerPurchases p) throws Exception {

        // Unicode Font (supports ₹ symbol)
        BaseFont unicodeFont = BaseFont.createFont(
                "src/main/resources/fonts/DejaVuSans.ttf",
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );

        Font tableHeaderFont = new Font(unicodeFont, 12, Font.BOLD);
        Font tableCellFont = new Font(unicodeFont, 11);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, baos);

        document.open();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // ---------------------------------------------------------
        // OPTIONAL LOGO (KEEP COMMENTED FOR NOW)
        // ---------------------------------------------------------
        /*
        Image logo = Image.getInstance("src/main/resources/static/logo.png");
        logo.scaleToFit(80, 80);
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);
        */

        document.add(Chunk.NEWLINE);

        // ---------------------------------------------------------
        // BUSINESS HEADER
        // ---------------------------------------------------------
        Paragraph header = new Paragraph(
                "DESHKAR PHOTO STUDIO & DIGITAL COLOR LAB",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)
        );
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        Paragraph address = new Paragraph(
                "Socialist Chowk, Main Road, Wardha, Maharashtra - 442001\n" +
                        "Phone: 9960845723 | Email: deshkarstudio1920@gmail.com",
                FontFactory.getFont(FontFactory.HELVETICA, 11)
        );
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);

        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));
        document.add(Chunk.NEWLINE);

        // ---------------------------------------------------------
        // INVOICE INFO
        // ---------------------------------------------------------
        PdfPTable invoiceTable = new PdfPTable(2);
        invoiceTable.setWidthPercentage(100);
        invoiceTable.setWidths(new float[]{2.5f, 3f});

        invoiceTable.addCell(getBoldCell("Invoice No:"));
        invoiceTable.addCell(getNormalCell(inv.getInvoiceNumber()));

        invoiceTable.addCell(getBoldCell("Invoice Date:"));
        invoiceTable.addCell(getNormalCell(df.format(inv.getInvoiceDate())));

        document.add(invoiceTable);
        document.add(Chunk.NEWLINE);

        // ---------------------------------------------------------
        // CUSTOMER DETAILS BOX
        // ---------------------------------------------------------
        PdfPTable customerBox = new PdfPTable(1);
        customerBox.setWidthPercentage(100);

        PdfPCell customerHeader = new PdfPCell(
                new Phrase("BILL TO", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12))
        );
        customerHeader.setBackgroundColor(new Color(230, 230, 230));
        customerHeader.setPadding(8);
        customerBox.addCell(customerHeader);

        PdfPCell cInfo = new PdfPCell();
        cInfo.setPadding(10);

        cInfo.addElement(new Paragraph("Name: " + inv.getCustomerName()));
        cInfo.addElement(new Paragraph("Phone: " + inv.getCustomerPhone()));
        cInfo.addElement(new Paragraph("Email: " + (inv.getCustomerEmail() == null || inv.getCustomerEmail().isEmpty()
                ? "NA" : inv.getCustomerEmail())));

        customerBox.addCell(cInfo);
        document.add(customerBox);

        document.add(Chunk.NEWLINE);

        // ---------------------------------------------------------
        // ITEMS TABLE
        // ---------------------------------------------------------
        PdfPTable itemsTable = new PdfPTable(3);
        itemsTable.setWidthPercentage(100);
        itemsTable.setWidths(new float[]{5f, 1.5f, 2f});

        itemsTable.addCell(getTableHeader("Description"));
        itemsTable.addCell(getTableHeader("Qty"));

        PdfPCell amountHeader = new PdfPCell(new Phrase("Amount (₹)", tableHeaderFont));
        amountHeader.setBackgroundColor(new Color(220, 220, 220));
        amountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        itemsTable.addCell(amountHeader);

        for (PurchaseItems item : p.getItems()) {
            itemsTable.addCell(getBorderCell(item.getProduct().getProductName()));
            itemsTable.addCell(getCenterBorderCell(String.valueOf(item.getQuantity())));
            itemsTable.addCell(getRightBorderCell("₹" + item.getItemPrice()));
        }

        document.add(itemsTable);

        // ---------------------------------------------------------
        // SUMMARY TABLE
        // ---------------------------------------------------------
        PdfPTable summary = new PdfPTable(2);
        summary.setWidthPercentage(40);
        summary.setHorizontalAlignment(Element.ALIGN_RIGHT);

        summary.addCell(getBoldCell("Subtotal:"));
        summary.addCell(getRightNoBorder("₹" + inv.getTotalAmount()));

        summary.addCell(getBoldCell("Paid:"));
        summary.addCell(getRightNoBorder("₹" + inv.getPaidAmount()));

        summary.addCell(getBoldCell("Balance:"));
        summary.addCell(getRightNoBorder("₹" + inv.getBalanceAmount()));

        summary.addCell(getBoldCell("Payment Method:"));
        summary.addCell(getRightNoBorder(inv.getPaymentMethod()));

        document.add(summary);

        document.add(new Chunk(ls));

        Paragraph thanks = new Paragraph("Thank you for your business!");
        thanks.setAlignment(Element.ALIGN_CENTER);
        document.add(thanks);

        document.close();

        return baos.toByteArray();
    }

    // ---------------------------------------------------------
    // Helper Cell Methods
    // ---------------------------------------------------------
    private static PdfPCell getBoldCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getNormalCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getTableHeader(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        cell.setBackgroundColor(new Color(220, 220, 220));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private static PdfPCell getBorderCell(String text) {
        return new PdfPCell(new Phrase(text));
    }

    private static PdfPCell getCenterBorderCell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private static PdfPCell getRightBorderCell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return c;
    }

    private static PdfPCell getRightNoBorder(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return c;
    }
}
