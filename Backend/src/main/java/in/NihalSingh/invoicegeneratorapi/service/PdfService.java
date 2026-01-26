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
            // ✅ FIX: USE STORED TEMPLATE HTML
            String html = invoice.getTemplateHtml();

            if (html == null || html.isBlank()) {
                throw new RuntimeException("Invoice template HTML is missing");
            }

            // ================= LOGO =================
            String logo = "";
            if (invoice.getCompany() != null &&
                    invoice.getCompany().getLogoBase64() != null &&
                    !invoice.getCompany().getLogoBase64().isBlank()) {

                String base64 = invoice.getCompany().getLogoBase64();
                if (base64.startsWith("data:image")) {
                    base64 = base64.substring(base64.indexOf(",") + 1);
                }

                logo = """
                        <img src="data:image/png;base64,%s"
                             style="width:120px;height:auto;margin-bottom:10px;" />
                        """.formatted(base64);
            }

            // ================= ITEMS =================
            StringBuilder items = new StringBuilder();
            double subTotal = 0;

            for (Item item : invoice.getItems()) {
                double rowTotal = item.getQty() * item.getAmount();
                subTotal += rowTotal;

                items.append("""
                        <tr>
                            <td>%s</td>
                            <td>%d</td>
                            <td>₹%.2f</td>
                            <td>₹%.2f</td>
                        </tr>
                        """.formatted(
                        item.getName(),
                        item.getQty(),
                        item.getAmount(),
                        rowTotal
                ));
            }

            // ================= TAX =================
            double cgst = invoice.getCgst() != null ? invoice.getCgst() : 0;
            double sgst = invoice.getSgst() != null ? invoice.getSgst() : 0;
            double igst = invoice.getIgst() != null ? invoice.getIgst() : 0;

            StringBuilder taxBlock = new StringBuilder();

            if (cgst > 0)
                taxBlock.append("<tr><td>CGST</td><td>₹").append(cgst).append("</td></tr>");

            if (sgst > 0)
                taxBlock.append("<tr><td>SGST</td><td>₹").append(sgst).append("</td></tr>");

            if (igst > 0)
                taxBlock.append("<tr><td>IGST</td><td>₹").append(igst).append("</td></tr>");

            double total = subTotal + cgst + sgst + igst;

            // ================= QR =================
            String qr = qrCodeService.generateQrBase64(
                    "Invoice: " + invoice.getInvoice().getNumber()
            );

            // ================= TEMPLATE BINDING =================
            html = html
                    .replace("{{invoice}}", invoice.getInvoice().getNumber())
                    .replace("{{invoiceDate}}",
                            invoice.getInvoice().getDate() != null ?
                                    invoice.getInvoice().getDate().toString() : "")
                    .replace("{{dueDate}}",
                            invoice.getInvoice().getDueDate() != null ?
                                    invoice.getInvoice().getDueDate().toString() : "")
                    .replace("{{company}}",
                            invoice.getCompany() != null ? invoice.getCompany().getName() : "")
                    .replace("{{address}}",
                            invoice.getCompany() != null ? invoice.getCompany().getAddress() : "")
                    .replace("{{billingName}}",
                            invoice.getBilling() != null ? invoice.getBilling().getName() : "")
                    .replace("{{billingPhone}}",
                            invoice.getBilling() != null ? invoice.getBilling().getPhone() : "")
                    .replace("{{billingAddress}}",
                            invoice.getBilling() != null ? invoice.getBilling().getAddress() : "")
                    .replace("{{logo}}", logo)
                    .replace("{{items}}", items.toString())
                    .replace("{{subtotal}}", String.format("%.2f", subTotal))
                    .replace("{{taxBlock}}", taxBlock.toString())
                    .replace("{{total}}", String.format("%.2f", total))
                    .replace("{{qr}}",
                            "<img width='120' src='data:image/png;base64," + qr + "'/>");

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
