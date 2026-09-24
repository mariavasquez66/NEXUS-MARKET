package application.domain.services.inventory;

import application.domain.entities.Inventory;
import application.domain.entities.Product;
import application.domain.entities.Warehouse;
import application.domain.exceptions.common.InvalidDataException;
import application.domain.exceptions.inventory.DamagedInventoryException;
import application.domain.exceptions.inventory.InsufficientStockException;
import application.domain.exceptions.inventory.InventoryNotFoundException;
import application.domain.exceptions.inventory.InvalidAdjustmentException;
import application.domain.exceptions.inventory.InvalidReservationException;
import application.domain.exceptions.inventory.NegativeStockException;
import application.domain.exceptions.product.ProductNotFoundException;
import application.domain.exceptions.warehouse.WarehouseNotFoundException;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public Inventory registerInventory(String productId, String warehouseId, int initialQuantity) {
        if (productId == null || productId.isBlank()) {
            throw new InvalidDataException("Product ID cannot be null or blank.");
        }
        if (warehouseId == null || warehouseId.isBlank()) {
            throw new InvalidDataException("Warehouse ID cannot be null or blank.");
        }
        if (initialQuantity < 0) {
            throw new NegativeStockException("Initial stock cannot be negative.");
        }

        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Warehouse warehouse = warehouseRepositoryPort.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        return inventoryRepositoryPort.findByProductIdAndWarehouseId(productId, warehouseId)
                .map(existing -> {
                    existing.addStock(initialQuantity);
                    return inventoryRepositoryPort.save(existing);
                })
                .orElseGet(() -> {
                    String id = UUID.randomUUID().toString();
                    Inventory inventory = new Inventory(id, product, warehouse, initialQuantity);
                    return inventoryRepositoryPort.save(inventory);
                });
    }

    public Inventory getInventory(String productId, String warehouseId) {
        if (productId == null || productId.isBlank() || warehouseId == null || warehouseId.isBlank()) {
            throw new InvalidDataException("Product ID and Warehouse ID must not be blank.");
        }
        return inventoryRepositoryPort.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException(productId, warehouseId));
    }

    public Inventory addStock(String productId, String warehouseId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("Quantity to add must be greater than zero.");
        }
        Inventory inventory = getInventory(productId, warehouseId);
        inventory.addStock(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory reserveStock(String productId, String warehouseId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("Reservation quantity must be greater than zero.");
        }
        Inventory inventory = getInventory(productId, warehouseId);

        if (inventory.isDamaged()) {
            throw new DamagedInventoryException(productId, warehouseId);
        }

        if (inventory.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException(productId, quantity, inventory.getAvailableQuantity());
        }

        inventory.reserve(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory confirmSale(String productId, String warehouseId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("Quantity to confirm sale must be greater than zero.");
        }
        Inventory inventory = getInventory(productId, warehouseId);
        inventory.confirmSale(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory cancelReservation(String productId, String warehouseId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("Quantity to cancel reservation must be greater than zero.");
        }
        Inventory inventory = getInventory(productId, warehouseId);
        if (inventory.getReservedQuantity() < quantity) {
            throw new InvalidReservationException("Cannot cancel reservation of " + quantity + " units; only "
                    + inventory.getReservedQuantity() + " units are reserved.");
        }
        inventory.cancelReservation(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory applyAdjustment(String productId, String warehouseId, int adjustment) {
        Inventory inventory = getInventory(productId, warehouseId);
        if (inventory.getQuantity() + adjustment < 0) {
            throw new NegativeStockException("Adjustment of " + adjustment + " would result in negative stock.");
        }
        try {
            inventory.applyAdjustment(adjustment);
        } catch (IllegalStateException ex) {
            throw new InvalidAdjustmentException(ex.getMessage());
        }
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory registerReturn(String productId, String warehouseId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("Return quantity must be greater than zero.");
        }
        Inventory inventory = getInventory(productId, warehouseId);
        inventory.addStock(quantity);
        return inventoryRepositoryPort.save(inventory);
    }

    public int getAvailableStock(String productId, String warehouseId) {
        Inventory inventory = getInventory(productId, warehouseId);
        return inventory.getAvailableQuantity();
    }

    public Inventory markAsDamaged(String productId, String warehouseId) {
        Inventory inventory = getInventory(productId, warehouseId);
        inventory.markAsDamaged();
        return inventoryRepositoryPort.save(inventory);
    }

    public Inventory markAsRepaired(String productId, String warehouseId) {
        Inventory inventory = getInventory(productId, warehouseId);
        inventory.markAsRepaired();
        return inventoryRepositoryPort.save(inventory);
    }
}
