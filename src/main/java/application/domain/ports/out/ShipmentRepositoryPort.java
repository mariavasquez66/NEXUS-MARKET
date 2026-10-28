package application.domain.ports.out;

import application.domain.entities.Shipment;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepositoryPort {
    Shipment save(Shipment shipment);
    Optional<Shipment> findById(String id);
    Optional<Shipment> findByOrderId(String orderId);
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    List<Shipment> findAll();
}
