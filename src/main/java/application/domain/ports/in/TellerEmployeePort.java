package application.domain.ports.in;

import application.domain.entities.Refund;
import application.domain.entities.Return;
import application.domain.entities.Seller;
import application.domain.entities.User;
import application.domain.entities.Warehouse;
import application.domain.valueobjects.WarehouseType;

import java.math.BigDecimal;
import java.util.List;

public interface TellerEmployeePort {
    Seller registerSeller(User adminUser, String id, String userId, String taxId);
    Warehouse createWarehouse(User adminUser, String id, String name, String address, WarehouseType type, String sellerId);
    void assignWarehouseToSeller(User adminUser, String sellerId, String warehouseId);

    User blockUser(User adminUser, String targetUserId);
    User activateUser(User adminUser, String targetUserId);
    User deleteUser(User adminUser, String targetUserId);

    Return approveReturn(User adminUser, String returnId);
    Return rejectReturn(User adminUser, String returnId);
    Refund processRefund(User adminUser, String returnId, BigDecimal amount, String paymentMethod);

    List<User> listAllUsers(User adminUser);
}
