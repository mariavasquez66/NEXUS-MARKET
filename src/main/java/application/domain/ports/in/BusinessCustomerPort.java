package application.domain.ports.in;

import application.domain.entities.Inventory;
import application.domain.entities.Product;
import application.domain.entities.User;
import application.domain.valueobjects.ProductType;

import java.math.BigDecimal;
import java.util.List;

public interface BusinessCustomerPort {
    Product registerProduct(User sellerUser, String id, String name, String description, BigDecimal price, ProductType type, List<String> variants);
    Product publishProduct(User sellerUser, String productId);
    Product suspendProduct(User sellerUser, String productId);
    Product discontinueProduct(User sellerUser, String productId);
    List<Product> getMyProducts(User sellerUser);

    List<Inventory> getMyInventory(User sellerUser);
    Inventory addStock(User sellerUser, String productId, String warehouseId, int quantity);
}

