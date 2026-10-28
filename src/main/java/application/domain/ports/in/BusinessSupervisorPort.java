package application.domain.ports.in;

import application.domain.entities.AuditLog;
import application.domain.entities.Order;
import application.domain.entities.Product;
import application.domain.entities.Shipment;
import application.domain.entities.User;

import java.util.List;

public interface BusinessSupervisorPort {
    List<Order> monitorOrders(User supervisorUser);
    List<Shipment> monitorShipments(User supervisorUser);
    List<Product> monitorCatalog(User supervisorUser);
    List<AuditLog> auditUserOperations(User supervisorUser, String targetUserId);
    List<AuditLog> auditEntity(User supervisorUser, String entityName, String entityId);
}
