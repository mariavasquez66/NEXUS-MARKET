package application.domain.services.seller;

import application.domain.entities.Seller;
import application.domain.entities.User;
import application.domain.entities.Warehouse;
import application.domain.exceptions.common.DuplicateEntityException;
import application.domain.exceptions.common.InsufficientPermissionException;
import application.domain.exceptions.common.InvalidDataException;
import application.domain.exceptions.seller.SelfRegistrationNotAllowedException;
import application.domain.exceptions.seller.SellerNotFoundException;
import application.domain.exceptions.seller.SellerWithoutWarehouseException;
import application.domain.exceptions.user.InactiveUserException;
import application.domain.exceptions.user.InvalidRoleException;
import application.domain.exceptions.user.UserNotFoundException;
import application.domain.exceptions.warehouse.WarehouseAlreadyAssignedException;
import application.domain.exceptions.warehouse.WarehouseNotFoundException;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.valueobjects.UserRole;
import application.domain.valueobjects.WarehouseType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SellerService {

    private final SellerRepositoryPort sellerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public Seller registerSeller(String id, String userId, String taxId, String adminUserId) {
        if (adminUserId == null || adminUserId.isBlank()) {
            throw new InsufficientPermissionException("An administrator ID is required to register a seller.");
        }
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Seller ID cannot be null or blank.");
        }
        if (userId == null || userId.isBlank()) {
            throw new InvalidDataException("User ID cannot be null or blank.");
        }
        if (taxId == null || taxId.isBlank()) {
            throw new InvalidDataException("Tax ID (NIT/RUC) cannot be null or blank.");
        }

        // Rule: Sellers cannot self-register; they are incorporated by an Administrator
        if (userId.equals(adminUserId)) {
            throw new SelfRegistrationNotAllowedException();
        }

        User adminUser = userRepositoryPort.findById(adminUserId)
                .orElseThrow(() -> new UserNotFoundException(adminUserId));

        if (!adminUser.isActive()) {
            throw new InactiveUserException("Administrator user is inactive or blocked: " + adminUserId);
        }

        if (!UserRole.ADMIN.equals(adminUser.getRole())) {
            throw new InsufficientPermissionException("Only administrators can register sellers (RG-03).");
        }

        if (sellerRepositoryPort.existsById(id)) {
            throw new DuplicateEntityException("Seller with identifier " + id + " already exists.");
        }
        if (sellerRepositoryPort.existsByUserId(userId)) {
            throw new DuplicateEntityException("A seller profile already exists for user ID " + userId);
        }
        if (sellerRepositoryPort.existsByTaxId(taxId)) {
            throw new DuplicateEntityException("Tax ID is already registered: " + taxId);
        }

        User sellerUser = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!sellerUser.isActive()) {
            throw new InactiveUserException("Associated user is inactive or blocked: " + userId);
        }

        if (!UserRole.SELLER.equals(sellerUser.getRole())) {
            throw new InvalidRoleException("User must have SELLER role to be registered as Seller. Actual role: "
                    + (sellerUser.getRole() != null ? sellerUser.getRole().getCode() : "null"));
        }

        Seller seller = new Seller(id, sellerUser, taxId);
        return sellerRepositoryPort.save(seller);
    }

    public Seller getSellerById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Seller ID cannot be null or blank.");
        }
        return sellerRepositoryPort.findById(id)
                .orElseThrow(() -> new SellerNotFoundException(id));
    }

    public List<Seller> getAllSellers() {
        return sellerRepositoryPort.findAll();
    }

    public Seller updateSeller(String id, String taxId) {
        Seller seller = getSellerById(id);
        if (taxId == null || taxId.isBlank()) {
            throw new InvalidDataException("Tax ID cannot be blank.");
        }
        if (!taxId.equalsIgnoreCase(seller.getTaxId())) {
            if (sellerRepositoryPort.existsByTaxId(taxId)) {
                throw new DuplicateEntityException("Tax ID already registered: " + taxId);
            }
            seller.setTaxId(taxId);
        }
        return sellerRepositoryPort.save(seller);
    }

    public Seller assignWarehouse(String sellerId, String warehouseId) {
        Seller seller = getSellerById(sellerId);

        if (warehouseId == null || warehouseId.isBlank()) {
            throw new InvalidDataException("Warehouse ID cannot be null or blank.");
        }

        Warehouse warehouse = warehouseRepositoryPort.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        if (warehouse.getSeller() != null && !warehouse.getSeller().getId().equals(sellerId)) {
            throw new WarehouseAlreadyAssignedException(
                    "Warehouse " + warehouseId + " is already assigned to another seller: " + warehouse.getSeller().getId());
        }

        warehouse.setSeller(seller);
        warehouse.setType(WarehouseType.SELLER);
        warehouseRepositoryPort.save(warehouse);

        seller.addWarehouse(warehouse);
        return sellerRepositoryPort.save(seller);
    }

    public void validateSellerHasWarehouses(String sellerId) {
        Seller seller = getSellerById(sellerId);
        if (seller.getWarehouses() == null || seller.getWarehouses().isEmpty()) {
            throw new SellerWithoutWarehouseException(sellerId);
        }
    }
}
