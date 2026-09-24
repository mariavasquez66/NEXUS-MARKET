package application.domain.entities;

import application.domain.valueobjects.WarehouseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Warehouse {
    private String id;
    private String name;
    private String address;
    private WarehouseType type;
    private Seller seller; // null if marketplace-owned

    public Warehouse(String id, String name, String address, WarehouseType type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public Warehouse(String id, String name, String address, WarehouseType type, Seller seller) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.seller = seller;
    }

    public boolean isMarketplaceWarehouse() {
        return WarehouseType.MARKETPLACE.equals(this.type);
    }

    public boolean isSellerWarehouse() {
        return WarehouseType.SELLER.equals(this.type);
    }
}