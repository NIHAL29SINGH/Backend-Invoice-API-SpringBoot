package in.NihalSingh.invoicegeneratorapi.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(Invoice invoice) {
        try {
            String html = buildHtml(invoice);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private String buildHtml(Invoice invoice) {

        StringBuilder itemsHtml = new StringBuilder();

        invoice.getItems().forEach(item -> {
            itemsHtml.append("""
                <tr>
                    <td>%s</td>
                    <td>%d</td>
                    <td>%.2f</td>
                    <td>%.2f</td>
                </tr>
            """.formatted(
                    item.getName(),
                    item.getQty(),
                    item.getAmount(),
                    item.getQty() * item.getAmount()
            ));
        });

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial; padding: 40px; }
                    h2 { color: #2c3e50; }
                    table { width: 100%%; border-collapse: collapse; margin-top: 20px; }
                    th, td { border: 1px solid #ddd; padding: 10px; }
                    th { background-color: #f5f5f5; }
                    .total { text-align: right; margin-top: 20px; font-size: 18px; }
                </style>
            </head>
            <body>

                <h2>Invoice</h2>

                <p><strong>Invoice No:</strong> %s</p>
                <p><strong>Billed To:</strong> %s</p>

                <table>
                    <tr>
                        <th>Item</th>
                        <th>Qty</th>
                        <th>Rate</th>
                        <th>Total</th>
                    </tr>
                    %s
                </table>

                <div class="total">
                    <strong>Total Amount: ₹%.2f</strong>
                </div>

            </body>
            </html>
        """.formatted(
                invoice.getInvoice().getNumber(),
                invoice.getBilling().getName(),   // ✅ FIXED
                itemsHtml.toString(),
                invoice.getItems().stream()
                        .mapToDouble(i -> i.getQty() * i.getAmount())
                        .sum()
        );
    }
}
