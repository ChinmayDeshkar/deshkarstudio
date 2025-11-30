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

            // Load Logo & compress (if needed)
            URL logoUrl = InvoicePdfGenerator.class.getResource("/static/images/logo/logo.png");
            if (logoUrl != null) {
                File scaledLogo = scaleAndCompressImage(logoUrl, 600, 600, 0.85f);
                ctx.setVariable("logoImage", scaledLogo.toURI().toString());
            } else {
                ctx.setVariable("logoImage", null);
            }

            // Render Thymeleaf HTML
            String html = templateEngine.process("invoice", ctx);

            // PDF Renderer
            ITextRenderer renderer = new ITextRenderer();

            // Base URL so CSS + Images resolve
            URL base = InvoicePdfGenerator.class.getResource("/static/");
            renderer.setDocumentFromString(html, base != null ? base.toExternalForm() : null);

            renderer.getSharedContext().setInteractive(false); // performance boost

            renderer.layout();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            renderer.createPDF(baos);

            return baos.toByteArray();

        } catch (Exception ex) {
            log.error("PDF generation failed", ex);
            throw new RuntimeException(ex);
        }
    }

    private static File scaleAndCompressImage(URL imageUrl, int maxWidth, int maxHeight, float quality) throws IOException {
        BufferedImage src = ImageIO.read(imageUrl);
        if (src == null) throw new IOException("Unable to read image: " + imageUrl);

        int w = src.getWidth();
        int h = src.getHeight();

        // Scaling
        double scale = Math.min(1.0, Math.min((double) maxWidth / w, (double) maxHeight / h));
        int tw = (int) (w * scale);
        int th = (int) (h * scale);

        boolean isPng = imageUrl.getPath().toLowerCase().endsWith(".png");
        int imageType = isPng ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        BufferedImage dst = new BufferedImage(tw, th, imageType);
        Graphics2D g = dst.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(src, 0, 0, tw, th, null);
        g.dispose();

        File tmp = File.createTempFile("scaled-", isPng ? ".png" : ".jpg");
        tmp.deleteOnExit();

        if (isPng) {
            ImageIO.write(dst, "png", tmp);
            return tmp;
        }

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
