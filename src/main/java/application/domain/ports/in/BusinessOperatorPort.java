package application.domain.ports.in;

import application.domain.entities.Inventory;
import application.domain.entities.Return;
import application.domain.entities.Shipment;
import application.domain.entities.User;

import java.util.List;

public interface BusinessOperatorPort {
    Shipment createShipment(User operatorUser, String orderId, String originWarehouseId, String destinationAddress);
    Shipment markShipmentInTransit(User operatorUser, String shipmentId);
    Shipment markShipmentDelivered(User operatorUser, String shipmentId);
    List<Shipment> getAllShipments(User operatorUser);

    Inventory adjustStock(User operatorUser, String productId, String warehouseId, int quantityAdjustment);
    Inventory markInventoryDamaged(User operatorUser, String productId, String warehouseId);
    Inventory markInventoryRepaired(User operatorUser, String productId, String warehouseId);

    Return receiveReturnedPackage(User operatorUser, String returnId);
}
