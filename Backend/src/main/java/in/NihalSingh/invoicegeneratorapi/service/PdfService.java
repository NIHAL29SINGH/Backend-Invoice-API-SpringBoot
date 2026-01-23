package in.NihalSingh.invoicegeneratorapi.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import in.NihalSingh.invoicegeneratorapi.entity.Invoice;
import in.NihalSingh.invoicegeneratorapi.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final QrCodeService qrCodeService;

    public byte[] generateInvoicePdf(Invoice invoice) {

        try {
            String html = invoice.getTemplate() != null
                    ? invoice.getTemplate().getHtmlTemplate()
                    : buildDefaultTemplate(invoice);

            // QR DATA
            String qrData =
                    "Invoice: " + invoice.getInvoice().getNumber() +
                            "\nCustomer: " + invoice.getBilling().getName() +
                            "\nTotal: " + invoice.getItems().stream()
                            .mapToDouble(i -> i.getQty() * i.getAmount())
                            .sum();

            String qrBase64 = qrCodeService.generateQrBase64(qrData);

            html = html.replace("{{qr}}",
                    "<img width='120' src='data:image/png;base64," + qrBase64 + "' />");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    private String buildDefaultTemplate(Invoice invoice) {

        double total = invoice.getItems()
                .stream()
                .mapToDouble(i -> i.getQty() * i.getAmount())
                .sum();

        return """
        <html>
        <body style="font-family:Arial;padding:40px">

        <div style="display:flex;justify-content:space-between">
            <h2>INVOICE</h2>
            {{qr}}
        </div>

        <p><b>Invoice No:</b> %s</p>
        <p><b>Customer:</b> %s</p>

        <h3>Total: ₹%.2f</h3>

        </body>
        </html>
        """.formatted(
                invoice.getInvoice().getNumber(),
                invoice.getBilling().getName(),
                total
        );
    }
}
