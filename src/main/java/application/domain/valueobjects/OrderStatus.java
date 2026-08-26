package application.domain.valueobjects;

public final class OrderStatus extends DomainCatalog {
    public static final OrderStatus CART = new OrderStatus("CART", "Cart", "Provisional product selection.");
    public static final OrderStatus PENDING_PAYMENT = new OrderStatus("PENDING_PAYMENT", "Pending Payment", "Awaiting financial confirmation.");
    public static final OrderStatus PAID = new OrderStatus("PAID", "Paid", "Start of fulfillment processes.");
    public static final OrderStatus DISPATCHED = new OrderStatus("DISPATCHED", "Dispatched", "Physical departure from the warehouse.");
    public static final OrderStatus DELIVERED = new OrderStatus("DELIVERED", "Delivered", "Successful completion of the delivery.");
    public static final OrderStatus FINALIZED = new OrderStatus("FINALIZED", "Finalized", "Order closed and unmodifiable.");

    private OrderStatus(String code, String name, String description) {
        super(code, name, description);
    }
} 

