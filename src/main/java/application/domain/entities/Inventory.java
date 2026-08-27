package application.domain.entities;

import application.domain.valueobjects.InventoryMovementType;
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

    public Inventory(Product product, Warehouse warehouse, int initialQuantity) {
        this.product = product;
        this.warehouse = warehouse;
        this.quantity = initialQuantity;
        this.reservedQuantity = 0;
    }

    public void addStock(int amount) {
        this.quantity += amount;
    }

    public void reserve(int amount) {
        if (this.quantity - this.reservedQuantity < amount) {
            throw new IllegalStateException("Inventario insuficiente para reservar " + amount + " unidades.");
        }
        this.reservedQuantity += amount;
    }

    public void confirmSale(int amount) {
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("No hay suficientes unidades reservadas para confirmar la venta.");
        }
        this.quantity -= amount;
        this.reservedQuantity -= amount;
    }

    public void cancelReservation(int amount) {
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("No hay suficientes unidades reservadas para cancelar.");
        }
        this.reservedQuantity -= amount;
    }

    public int getAvailableQuantity() {
        return this.quantity - this.reservedQuantity;
    }

    public void applyAdjustment(int adjustment) {
        if (this.quantity + adjustment < 0) {
            throw new IllegalStateException("Ajuste no permitido: stock negativo.");
        }
        this.quantity += adjustment;
    }
}