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
            String html = invoice.getTemplate().getHtmlTemplate();

            /* ================= LOGO ================= */
            String logo = "";
            if (invoice.getCompany() != null &&
                    invoice.getCompany().getLogoBase64() != null &&
                    !invoice.getCompany().getLogoBase64().isEmpty()) {

                logo = "<img style='width:100px;height:auto' src='data:image/png;base64,"
                        + invoice.getCompany().getLogoBase64() + "'/>";
            }

            /* ================= ITEMS ================= */
            StringBuilder items = new StringBuilder();
            double total = 0;

            for (Item i : invoice.getItems()) {
                double rowTotal = i.getQty() * i.getAmount();
                total += rowTotal;

                items.append("""
                    <tr>
                        <td>%s</td>
                        <td>%d</td>
                        <td>₹%.2f</td>
                        <td>₹%.2f</td>
                    </tr>
                """.formatted(
                        i.getName(),
                        i.getQty(),
                        i.getAmount(),
                        rowTotal
                ));
            }

            /* ================= QR ================= */
            String qr = qrCodeService.generateQrBase64(
                    "Invoice: " + invoice.getInvoice().getNumber()
            );

            /* ================= TEMPLATE REPLACEMENT ================= */
            html = html
                    // Invoice
                    .replace("{{invoice}}",
                            invoice.getInvoice().getNumber())

                    .replace("{{invoiceDate}}",
                            invoice.getInvoice().getDate() != null
                                    ? invoice.getInvoice().getDate().toString()
                                    : "")

                    .replace("{{dueDate}}",
                            invoice.getInvoice().getDueDate() != null
                                    ? invoice.getInvoice().getDueDate().toString()
                                    : "")

                    // Company
                    .replace("{{company}}",
                            invoice.getCompany() != null ? invoice.getCompany().getName() : "")

                    .replace("{{address}}",
                            invoice.getCompany() != null ? invoice.getCompany().getAddress() : "")

                    .replace("{{logo}}", logo)

                    // Billing
                    .replace("{{billingName}}",
                            invoice.getBilling() != null ? invoice.getBilling().getName() : "")

                    .replace("{{billingPhone}}",
                            invoice.getBilling() != null ? invoice.getBilling().getPhone() : "")

                    .replace("{{billingAddress}}",
                            invoice.getBilling() != null ? invoice.getBilling().getAddress() : "")

                    // Items
                    .replace("{{items}}", items.toString())

                    // Totals
                    .replace("{{total}}", String.valueOf(total))

                    // Bank (optional)
                    .replace("{{accountName}}", "Your Company Name")
                    .replace("{{accountNumber}}", "1234567890")
                    .replace("{{ifsc}}", "SBIN0000123")

                    // QR
                    .replace("{{qr}}",
                            "<img width='100' src='data:image/png;base64," + qr + "'/>");

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
}
