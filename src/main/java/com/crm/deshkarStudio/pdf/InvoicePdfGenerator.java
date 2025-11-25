package com.crm.deshkarStudio.pdf;

import com.crm.deshkarStudio.model.CustomerPurchases;
import com.crm.deshkarStudio.model.Invoice;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

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

            // ------------------------ Load Logo (scale if needed) ------------------------
            URL logoUrl = InvoicePdfGenerator.class.getResource("/static/images/logo/logo.png");
            if (logoUrl != null) {
                File scaledLogo = scaleAndCompressImage(logoUrl, 600, 600, 0.85f); // logos are small
                ctx.setVariable("logoImage", scaledLogo.toURI().toString());
            } else {
                ctx.setVariable("logoImage", null);
            }

            // ------------------------ Load Background (scale heavy image) ------------------------
            URL bgUrl = InvoicePdfGenerator.class.getResource("/static/images/templates/template.jpg");
            if (bgUrl != null) {
                // A5 page — 1200x1700 is optimal
                File scaledBg = scaleAndCompressImage(bgUrl, 1200, 1700, 0.80f);
                ctx.setVariable("bgImage", scaledBg.toURI().toString());
            } else {
                ctx.setVariable("bgImage", null);
            }

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

    private static File scaleAndCompressImage(URL imageUrl, int maxWidth, int maxHeight, float quality) throws IOException {
        BufferedImage src = ImageIO.read(imageUrl);
        if (src == null) throw new IOException("Unable to read image: " + imageUrl);

        int w = src.getWidth();
        int h = src.getHeight();

        // scaling
        double scale = Math.min(1.0, Math.min((double) maxWidth / w, (double) maxHeight / h));
        int tw = (int) (w * scale);
        int th = (int) (h * scale);

        // preserve transparency if PNG
        boolean isPng = imageUrl.getPath().toLowerCase().endsWith(".png");
        int imageType = isPng ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        BufferedImage dst = new BufferedImage(tw, th, imageType);
        Graphics2D g = dst.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(src, 0, 0, tw, th, null);
        g.dispose();

        // create temp output file
        File tmp = File.createTempFile("scaled-", isPng ? ".png" : ".jpg");
        tmp.deleteOnExit();

        if (isPng) {
            // ---- PNG: write without compression loss, transparency preserved ----
            ImageIO.write(dst, "png", tmp);
            return tmp;
        }

        // ---- JPG: compressed ----
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = writers.hasNext() ? writers.next() : null;

        if (writer == null) {
            ImageIO.write(dst, "jpg", tmp);
            return tmp;
        }

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(tmp)) {
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }
            writer.write(null, new IIOImage(dst, null, null), param);
        } finally {
            writer.dispose();
        }

        return tmp;
    }


}
