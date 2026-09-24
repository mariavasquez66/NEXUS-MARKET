package application.domain.services.warehouse;

import application.domain.entities.Seller;
import application.domain.entities.Warehouse;
import application.domain.exceptions.common.DuplicateEntityException;
import application.domain.exceptions.common.InvalidDataException;
import application.domain.exceptions.seller.SellerNotFoundException;
import application.domain.exceptions.warehouse.InvalidWarehouseTypeException;
import application.domain.exceptions.warehouse.WarehouseNotFoundException;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.valueobjects.WarehouseType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;

    public Warehouse createWarehouse(String id, String name, String address, WarehouseType type, String sellerId) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Warehouse ID cannot be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Warehouse name cannot be null or blank.");
        }
        if (address == null || address.isBlank()) {
            throw new InvalidDataException("Warehouse address cannot be null or blank.");
        }
        if (type == null) {
            throw new InvalidWarehouseTypeException("Warehouse type cannot be null.");
        }

        if (warehouseRepositoryPort.existsById(id)) {
            throw new DuplicateEntityException("Warehouse with ID " + id + " already exists.");
        }

        Seller seller = null;
        if (WarehouseType.SELLER.equals(type)) {
            if (sellerId == null || sellerId.isBlank()) {
                throw new InvalidDataException("Seller ID is required for a SELLER warehouse.");
            }
            seller = sellerRepositoryPort.findById(sellerId)
                    .orElseThrow(() -> new SellerNotFoundException(sellerId));
        }

        Warehouse warehouse = new Warehouse(id, name, address, type, seller);
        Warehouse savedWarehouse = warehouseRepositoryPort.save(warehouse);

        if (seller != null) {
            seller.addWarehouse(savedWarehouse);
            sellerRepositoryPort.save(seller);
        }

        return savedWarehouse;
    }

    public Warehouse getWarehouseById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Warehouse ID cannot be null or blank.");
        }
        return warehouseRepositoryPort.findById(id)
                .orElseThrow(() -> new WarehouseNotFoundException(id));
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepositoryPort.findAll();
    }

    public Warehouse updateWarehouse(String id, String name, String address) {
        Warehouse warehouse = getWarehouseById(id);
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Warehouse name cannot be blank.");
        }
        if (address == null || address.isBlank()) {
            throw new InvalidDataException("Warehouse address cannot be blank.");
        }
        warehouse.setName(name);
        warehouse.setAddress(address);
        return warehouseRepositoryPort.save(warehouse);
    }

    public List<Warehouse> listWarehousesBySeller(String sellerId) {
        if (sellerId == null || sellerId.isBlank()) {
            throw new InvalidDataException("Seller ID cannot be null or blank.");
        }
        if (!sellerRepositoryPort.existsById(sellerId)) {
            throw new SellerNotFoundException(sellerId);
        }
        return warehouseRepositoryPort.findBySellerId(sellerId);
    }
}
