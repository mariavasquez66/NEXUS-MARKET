package application.domain.valueobjects;

public final class OrderStatus extends DomainCatalog {
    public static final OrderStatus CART = new OrderStatus("CART", "Cart", "Provisional product selection.");
    public static final OrderStatus PENDING_PAYMENT = new OrderStatus("PENDING_PAYMENT", "Pending Payment", "Awaiting financial confirmation.");
    public static final OrderStatus PAID = new OrderStatus("PAID", "Paid", "Start of fulfillment processes.");
    public static final OrderStatus DISPATCHED = new OrderStatus("DISPATCHED", "Dispatched", "Physical departure from the warehouse.");
    public static final OrderStatus DELIVERED = new OrderStatus("DELIVERED", "Delivered", "Successful completion of the delivery.");
    public static final OrderStatus FINALIZED = new OrderStatus("FINALIZED", "Finalized", "Order closed and unmodifiable.");
    public static final OrderStatus CANCELLED = new OrderStatus("CANCELLED", "Cancelled", "Order has been cancelled.");

    private OrderStatus(String code, String name, String description) {
        super(code, name, description);
    }

    public static OrderStatus fromCode(String code) {
        if ("CART".equalsIgnoreCase(code)) return CART;
        if ("PENDING_PAYMENT".equalsIgnoreCase(code)) return PENDING_PAYMENT;
        if ("PAID".equalsIgnoreCase(code)) return PAID;
        if ("DISPATCHED".equalsIgnoreCase(code)) return DISPATCHED;
        if ("DELIVERED".equalsIgnoreCase(code)) return DELIVERED;
        if ("FINALIZED".equalsIgnoreCase(code)) return FINALIZED;
        if ("CANCELLED".equalsIgnoreCase(code)) return CANCELLED;
        throw new IllegalArgumentException("Invalid order status code: " + code);
    }
}
