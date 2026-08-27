package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Warehouse {
    private String id;
    private String name;
    private String address;
    private String type; // "MARKETPLACE" o "SELLER"
    private Seller seller; // null si es del Marketplace

    public Warehouse(String id, String name, String address, String type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }
}