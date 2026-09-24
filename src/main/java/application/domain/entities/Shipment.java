package application.domain.entities;

import application.domain.valueobjects.ShipmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Shipment {
    private String id;
    private Order order;
    private String trackingNumber;
    private Warehouse originWarehouse;
    private String destinationAddress;
    private ShipmentStatus status;

    public Shipment(Order order, Warehouse originWarehouse, String destinationAddress) {
        this.order = order;
        this.originWarehouse = originWarehouse;
        this.destinationAddress = destinationAddress;
        this.status = ShipmentStatus.PENDING;
    }

    public Shipment(String id, Order order, Warehouse originWarehouse, String destinationAddress) {
        this(order, originWarehouse, destinationAddress);
        this.id = id;
    }

    public void startShipping(String trackingNumber) {
        if (!ShipmentStatus.PENDING.equals(this.status)) {
            throw new IllegalStateException("Shipment is already started or delivered.");
        }
        this.trackingNumber = trackingNumber;
        this.status = ShipmentStatus.IN_TRANSIT;
    }

    public void confirmDelivery() {
        if (!ShipmentStatus.IN_TRANSIT.equals(this.status)) {
            throw new IllegalStateException("Shipment must be in transit to confirm delivery.");
        }
        this.status = ShipmentStatus.DELIVERED;
    }

    public boolean isDelivered() {
        return ShipmentStatus.DELIVERED.equals(this.status);
    }

    public boolean isInTransit() {
        return ShipmentStatus.IN_TRANSIT.equals(this.status);
    }

    public boolean isPending() {
        return ShipmentStatus.PENDING.equals(this.status);
    }
}