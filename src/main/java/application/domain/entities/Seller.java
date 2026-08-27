package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Seller {
    private String id;
    private User user;
    private String taxId;
    private List<Warehouse> warehouses;

    public Seller(User user, String taxId) {
        this.user = user;
        this.taxId = taxId;
        this.warehouses = new ArrayList<>();
    }

    public void addWarehouse(Warehouse warehouse) {
        this.warehouses.add(warehouse);
    }
}