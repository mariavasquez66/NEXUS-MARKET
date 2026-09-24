package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inventory {
    private String id;
    private Product product;
    private Warehouse warehouse;
    private int quantity;
    private int reservedQuantity;
    private boolean damaged;

    public Inventory(Product product, Warehouse warehouse, int initialQuantity) {
        this.product = product;
        this.warehouse = warehouse;
        this.quantity = initialQuantity;
        this.reservedQuantity = 0;
        this.damaged = false;
    }

    public Inventory(String id, Product product, Warehouse warehouse, int initialQuantity) {
        this.id = id;
        this.product = product;
        this.warehouse = warehouse;
        this.quantity = initialQuantity;
        this.reservedQuantity = 0;
        this.damaged = false;
    }

    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to add cannot be negative.");
        }
        this.quantity += amount;
    }

    public void reserve(int amount) {
        if (this.damaged) {
            throw new IllegalStateException("Cannot reserve damaged inventory.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Reservation amount must be greater than zero.");
        }
        if (this.quantity - this.reservedQuantity < amount) {
            throw new IllegalStateException("Insufficient inventory to reserve " + amount + " units.");
        }
        this.reservedQuantity += amount;
    }

    public void confirmSale(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Sale amount must be greater than zero.");
        }
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Not enough reserved units to confirm sale.");
        }
        this.quantity -= amount;
        this.reservedQuantity -= amount;
    }

    public void cancelReservation(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Cancellation amount must be greater than zero.");
        }
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Not enough reserved units to cancel reservation.");
        }
        this.reservedQuantity -= amount;
    }

    public int getAvailableQuantity() {
        if (this.damaged) {
            return 0;
        }
        return this.quantity - this.reservedQuantity;
    }

    public void applyAdjustment(int adjustment) {
        if (this.quantity + adjustment < 0) {
            throw new IllegalStateException("Adjustment not permitted: results in negative stock.");
        }
        this.quantity += adjustment;
    }

    public void markAsDamaged() {
        this.damaged = true;
    }

    public void markAsRepaired() {
        this.damaged = false;
    }
}