package application.domain.ports.out;

import application.domain.entities.Invoice;
import java.util.Optional;

public interface InvoiceRepositoryPort {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(String id);
    Optional<Invoice> findByOrderId(String orderId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}

