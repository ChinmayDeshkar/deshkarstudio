package com.crm.deshkarStudio.pdf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

@Configuration
public class PdfConfig {

    private final TemplateEngine templateEngine;

    public PdfConfig(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Bean
    public CommandLineRunner invoiceTemplateInit() {
        return args -> com.crm.deshkarStudio.pdf.InvoicePdfGenerator.setTemplateEngine(templateEngine);
    }
}
