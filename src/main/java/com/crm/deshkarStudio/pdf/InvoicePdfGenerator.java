package com.crm.deshkarStudio.pdf;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.Invoice;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URL;

@Slf4j
public class InvoicePdfGenerator {

    @Setter
    private static TemplateEngine templateEngine;

    public static byte[] generatePDF(Invoice invoice, CustomerPurchases purchase) {

        if (templateEngine == null) {
            throw new IllegalStateException("TemplateEngine not initialized.");
        }

        try {
            Context ctx = new Context();
            ctx.setVariable("invoice", invoice);
            ctx.setVariable("purchase", purchase);

            // ------------------------ Load Logo ------------------------
            URL logoUrl = InvoicePdfGenerator.class
                    .getResource("/static/images/logo/logo.png");
            ctx.setVariable("logoImage", logoUrl != null ? logoUrl.toExternalForm() : null);

            // ------------------------ Load Background ------------------------
            URL bgUrl = InvoicePdfGenerator.class
                    .getResource("/static/images/templates/template.jpg");
            ctx.setVariable("bgImage", bgUrl != null ? bgUrl.toExternalForm() : null);

            // ------------------------ Process HTML ------------------------
            String html = templateEngine.process("invoice", ctx);

            // ------------------------ Renderer ------------------------
            ITextRenderer renderer = new ITextRenderer();

            // Register font
            registerFont(renderer, "/static/fonts/DejaVuSans.ttf");

            // Base URL so CSS + Images resolve
            URL base = InvoicePdfGenerator.class.getResource("/static/");
            String baseUrl = (base != null) ? base.toExternalForm() : null;

            renderer.setDocumentFromString(html, baseUrl);
            renderer.layout();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            renderer.createPDF(baos);

            return baos.toByteArray();

        } catch (Exception ex) {
            log.error("PDF generation failed", ex);
            throw new RuntimeException(ex);
        }
    }

    private static void registerFont(ITextRenderer renderer, String resourcePath) {
        try (InputStream is = InvoicePdfGenerator.class.getResourceAsStream(resourcePath)) {

            if (is == null) {
                log.warn("Font not found: " + resourcePath);
                return;
            }

            File tmp = File.createTempFile("font-", ".ttf");
            tmp.deleteOnExit();

            try (OutputStream os = new FileOutputStream(tmp)) {
                is.transferTo(os);
            }

            renderer.getFontResolver().addFont(tmp.getAbsolutePath(), true);

        } catch (Exception e) {
            log.warn("Font registration failed: " + e.getMessage());
        }
    }
}
