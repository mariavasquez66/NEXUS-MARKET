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

    public void startShipping(String trackingNumber) {
        this.trackingNumber = trackingNumber;
        this.status = ShipmentStatus.IN_TRANSIT;
    }

    public void confirmDelivery() {
        this.status = ShipmentStatus.DELIVERED;
    }
}