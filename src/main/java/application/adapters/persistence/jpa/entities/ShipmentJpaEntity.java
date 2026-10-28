package application.adapters.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class ShipmentJpaEntity {

    @Id
    @Column(length = 64)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private OrderJpaEntity order;

    @Column(nullable = false, unique = true, length = 100)
    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_warehouse_id", nullable = false)
    private WarehouseJpaEntity originWarehouse;

    @Column(nullable = false, length = 255)
    private String destinationAddress;

    @Column(nullable = false, length = 50)
    private String status;

    private LocalDateTime dispatchDate;
    private LocalDateTime deliveryDate;

    public ShipmentJpaEntity() {
    }

    public ShipmentJpaEntity(String id, OrderJpaEntity order, String trackingNumber, WarehouseJpaEntity originWarehouse, String destinationAddress, String status, LocalDateTime dispatchDate, LocalDateTime deliveryDate) {
        this.id = id;
        this.order = order;
        this.trackingNumber = trackingNumber;
        this.originWarehouse = originWarehouse;
        this.destinationAddress = destinationAddress;
        this.status = status;
        this.dispatchDate = dispatchDate;
        this.deliveryDate = deliveryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderJpaEntity getOrder() {
        return order;
    }

    public void setOrder(OrderJpaEntity order) {
        this.order = order;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public WarehouseJpaEntity getOriginWarehouse() {
        return originWarehouse;
    }

    public void setOriginWarehouse(WarehouseJpaEntity originWarehouse) {
        this.originWarehouse = originWarehouse;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDateTime dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}

