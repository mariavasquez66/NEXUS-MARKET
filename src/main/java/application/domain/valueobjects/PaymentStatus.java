package application.domain.valueobjects;

public final class PaymentStatus extends DomainCatalog {
    public static final PaymentStatus PENDING = new PaymentStatus("PENDING", "Pending", "Payment not confirmed.");
    public static final PaymentStatus CONFIRMED = new PaymentStatus("CONFIRMED", "Confirmed", "Payment successfully validated.");
    public static final PaymentStatus REJECTED = new PaymentStatus("REJECTED", "Rejected", "Payment not accepted.");

    private PaymentStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static PaymentStatus fromCode(String code) {
        if ("PENDING".equalsIgnoreCase(code)) return PENDING;
        if ("CONFIRMED".equalsIgnoreCase(code)) return CONFIRMED;
        if ("REJECTED".equalsIgnoreCase(code)) return REJECTED;
        throw new IllegalArgumentException("Invalid payment status code: " + code);
    }
}