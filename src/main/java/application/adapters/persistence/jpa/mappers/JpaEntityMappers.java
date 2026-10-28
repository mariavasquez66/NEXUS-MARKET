package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.*;
import application.domain.entities.*;
import application.domain.valueobjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JpaEntityMappers {

    private JpaEntityMappers() {
    }

    // --- User Mapper ---
    public static UserJpaEntity toJpaEntity(User domain) {
        if (domain == null) return null;
        return new UserJpaEntity(
                domain.getId(),
                domain.getFullName(),
                domain.getEmail(),
                domain.getRole() != null ? domain.getRole().getCode() : null,
                domain.getStatus() != null ? domain.getStatus().getCode() : null,
                domain.isDeleted()
        );
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                UserRole.fromCode(entity.getRole()),
                UserStatus.fromCode(entity.getStatus()),
                entity.isDeleted()
        );
    }

    // --- Buyer Mapper ---
    public static BuyerJpaEntity toJpaEntity(Buyer domain) {
        if (domain == null) return null;
        return new BuyerJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getUser()),
                domain.getMainAddress(),
                new ArrayList<>(domain.getAdditionalAddresses()),
                domain.getCommercialStatus() != null ? domain.getCommercialStatus().getCode() : null
        );
    }

    public static Buyer toDomain(BuyerJpaEntity entity) {
        if (entity == null) return null;
        return new Buyer(
                entity.getId(),
                toDomain(entity.getUser()),
                entity.getMainAddress(),
                new ArrayList<>(entity.getAdditionalAddresses()),
                CommercialStatus.fromCode(entity.getCommercialStatus())
        );
    }

    // --- Seller Mapper ---
    public static SellerJpaEntity toJpaEntity(Seller domain) {
        if (domain == null) return null;
        return new SellerJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getUser()),
                domain.getTaxId()
        );
    }

    public static Seller toDomain(SellerJpaEntity entity, List<Warehouse> warehouses) {
        if (entity == null) return null;
        return new Seller(
                entity.getId(),
                toDomain(entity.getUser()),
                entity.getTaxId(),
                warehouses != null ? warehouses : new ArrayList<>()
        );
    }

    // --- Warehouse Mapper ---
    public static WarehouseJpaEntity toJpaEntity(Warehouse domain) {
        if (domain == null) return null;
        return new WarehouseJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getAddress(),
                domain.getType() != null ? domain.getType().getCode() : null,
                toJpaEntity(domain.getSeller())
        );
    }

    public static Warehouse toDomain(WarehouseJpaEntity entity) {
        if (entity == null) return null;
        Seller seller = entity.getSeller() != null ? toDomain(entity.getSeller(), new ArrayList<>()) : null;
        return new Warehouse(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                WarehouseType.fromCode(entity.getType()),
                seller
        );
    }

    // --- Product Mapper ---
    public static ProductJpaEntity toJpaEntity(Product domain) {
        if (domain == null) return null;
        return new ProductJpaEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getPrice(),
                domain.getType() != null ? domain.getType().getCode() : null,
                domain.getStatus() != null ? domain.getStatus().getCode() : null,
                toJpaEntity(domain.getSeller()),
                new ArrayList<>(domain.getVariants())
        );
    }

    public static Product toDomain(ProductJpaEntity entity) {
        if (entity == null) return null;
        Seller seller = entity.getSeller() != null ? toDomain(entity.getSeller(), new ArrayList<>()) : null;
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                ProductType.fromCode(entity.getType()),
                ProductStatus.fromCode(entity.getStatus()),
                seller,
                new ArrayList<>(entity.getVariants())
        );
    }

    // --- Inventory Mapper ---
    public static InventoryJpaEntity toJpaEntity(Inventory domain) {
        if (domain == null) return null;
        return new InventoryJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getProduct()),
                toJpaEntity(domain.getWarehouse()),
                domain.getAvailableQuantity(),
                domain.getReservedQuantity(),
                domain.isDamaged()
        );
    }

    public static Inventory toDomain(InventoryJpaEntity entity) {
        if (entity == null) return null;
        return new Inventory(
                entity.getId(),
                toDomain(entity.getProduct()),
                toDomain(entity.getWarehouse()),
                entity.getAvailableQuantity(),
                entity.getReservedQuantity(),
                entity.isDamaged()
        );
    }

    // --- ShoppingCart Mapper ---
    public static ShoppingCartJpaEntity toJpaEntity(ShoppingCart domain) {
        if (domain == null) return null;
        ShoppingCartJpaEntity cartEntity = new ShoppingCartJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getBuyer()),
                new ArrayList<>()
        );
        if (domain.getItems() != null) {
            List<CartItemJpaEntity> itemEntities = domain.getItems().stream()
                    .map(item -> new CartItemJpaEntity(cartEntity, item.getProductId(), item.getQuantity(), item.getUnitPrice()))
                    .collect(Collectors.toList());
            cartEntity.setItems(itemEntities);
        }
        return cartEntity;
    }

    public static ShoppingCart toDomain(ShoppingCartJpaEntity entity) {
        if (entity == null) return null;
        List<ShoppingCart.CartItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            items = entity.getItems().stream()
                    .map(i -> new ShoppingCart.CartItem(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                    .collect(Collectors.toList());
        }
        return new ShoppingCart(
                entity.getId(),
                toDomain(entity.getBuyer()),
                items
        );
    }

    // --- Order Mapper ---
    public static OrderJpaEntity toJpaEntity(Order domain) {
        if (domain == null) return null;
        OrderJpaEntity orderEntity = new OrderJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getBuyer()),
                domain.getTotalAmount(),
                domain.getStatus() != null ? domain.getStatus().getCode() : null,
                domain.getPaymentStatus() != null ? domain.getPaymentStatus().getCode() : null,
                domain.getCreatedAt(),
                domain.getPaidAt(),
                domain.getDispatchedAt(),
                domain.getDeliveredAt(),
                domain.getCancelledAt(),
                new ArrayList<>()
        );
        if (domain.getItems() != null) {
            List<OrderItemJpaEntity> itemEntities = domain.getItems().stream()
                    .map(item -> new OrderItemJpaEntity(orderEntity, item.getProductId(), item.getQuantity(), item.getUnitPrice()))
                    .collect(Collectors.toList());
            orderEntity.setItems(itemEntities);
        }
        return orderEntity;
    }

    public static Order toDomain(OrderJpaEntity entity) {
        if (entity == null) return null;
        List<ShoppingCart.CartItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            items = entity.getItems().stream()
                    .map(i -> new ShoppingCart.CartItem(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                    .collect(Collectors.toList());
        }
        return new Order(
                entity.getId(),
                toDomain(entity.getBuyer()),
                items,
                entity.getTotalAmount(),
                OrderStatus.fromCode(entity.getStatus()),
                PaymentStatus.fromCode(entity.getPaymentStatus()),
                entity.getCreatedAt(),
                entity.getPaidAt(),
                entity.getDispatchedAt(),
                entity.getDeliveredAt(),
                entity.getCancelledAt()
        );
    }

    // --- Invoice Mapper ---
    public static InvoiceJpaEntity toJpaEntity(Invoice domain) {
        if (domain == null) return null;
        return new InvoiceJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getOrder()),
                domain.getInvoiceNumber(),
                domain.getSubtotal(),
                domain.getTaxes(),
                domain.getTotal(),
                domain.getIssueDate(),
                domain.isVoided()
        );
    }

    public static Invoice toDomain(InvoiceJpaEntity entity) {
        if (entity == null) return null;
        return new Invoice(
                entity.getId(),
                toDomain(entity.getOrder()),
                entity.getInvoiceNumber(),
                entity.getSubtotal(),
                entity.getTaxes(),
                entity.getTotal(),
                entity.getIssueDate(),
                entity.isVoided()
        );
    }

    // --- Shipment Mapper ---
    public static ShipmentJpaEntity toJpaEntity(Shipment domain) {
        if (domain == null) return null;
        return new ShipmentJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getOrder()),
                domain.getTrackingNumber(),
                toJpaEntity(domain.getOriginWarehouse()),
                domain.getDestinationAddress(),
                domain.getStatus() != null ? domain.getStatus().getCode() : null,
                domain.getDispatchDate(),
                domain.getDeliveryDate()
        );
    }

    public static Shipment toDomain(ShipmentJpaEntity entity) {
        if (entity == null) return null;
        return new Shipment(
                entity.getId(),
                toDomain(entity.getOrder()),
                entity.getTrackingNumber(),
                toDomain(entity.getOriginWarehouse()),
                entity.getDestinationAddress(),
                ShipmentStatus.fromCode(entity.getStatus()),
                entity.getDispatchDate(),
                entity.getDeliveryDate()
        );
    }

    // --- Return Mapper ---
    public static ReturnJpaEntity toJpaEntity(Return domain) {
        if (domain == null) return null;
        return new ReturnJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getOrder()),
                domain.getReason(),
                domain.getStatus() != null ? domain.getStatus().getCode() : null,
                domain.getRequestDate(),
                domain.getResolutionDate()
        );
    }

    public static Return toDomain(ReturnJpaEntity entity) {
        if (entity == null) return null;
        return new Return(
                entity.getId(),
                toDomain(entity.getOrder()),
                entity.getReason(),
                ReturnStatus.fromCode(entity.getStatus()),
                entity.getRequestDate(),
                entity.getResolutionDate()
        );
    }

    // --- Refund Mapper ---
    public static RefundJpaEntity toJpaEntity(Refund domain) {
        if (domain == null) return null;
        return new RefundJpaEntity(
                domain.getId(),
                toJpaEntity(domain.getReturnRequest()),
                domain.getAmount(),
                domain.getPaymentMethod(),
                domain.isProcessed()
        );
    }

    public static Refund toDomain(RefundJpaEntity entity) {
        if (entity == null) return null;
        return new Refund(
                entity.getId(),
                toDomain(entity.getReturnRequest()),
                entity.getAmount(),
                entity.getPaymentMethod(),
                entity.isProcessed()
        );
    }
}
