package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.BuyerJpaEntity;
import application.adapters.persistence.jpa.entities.CartItemJpaEntity;
import application.adapters.persistence.jpa.entities.InventoryJpaEntity;
import application.adapters.persistence.jpa.entities.InvoiceJpaEntity;
import application.adapters.persistence.jpa.entities.OrderItemJpaEntity;
import application.adapters.persistence.jpa.entities.OrderJpaEntity;
import application.adapters.persistence.jpa.entities.ProductJpaEntity;
import application.adapters.persistence.jpa.entities.RefundJpaEntity;
import application.adapters.persistence.jpa.entities.ReturnJpaEntity;
import application.adapters.persistence.jpa.entities.SellerJpaEntity;
import application.adapters.persistence.jpa.entities.ShipmentJpaEntity;
import application.adapters.persistence.jpa.entities.ShoppingCartJpaEntity;
import application.adapters.persistence.jpa.entities.UserJpaEntity;
import application.adapters.persistence.jpa.entities.WarehouseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public final class Repositories {

    @Repository
    public interface UserSpringDataRepository extends JpaRepository<UserJpaEntity, String> {
        Optional<UserJpaEntity> findByEmail(String email);
        boolean existsByEmail(String email);
    }

    @Repository
    public interface BuyerSpringDataRepository extends JpaRepository<BuyerJpaEntity, String> {
        Optional<BuyerJpaEntity> findByUserId(String userId);
        boolean existsByUserId(String userId);
    }

    @Repository
    public interface SellerSpringDataRepository extends JpaRepository<SellerJpaEntity, String> {
        Optional<SellerJpaEntity> findByUserId(String userId);
        Optional<SellerJpaEntity> findByTaxId(String taxId);
        boolean existsByUserId(String userId);
        boolean existsByTaxId(String taxId);
    }

    @Repository
    public interface WarehouseSpringDataRepository extends JpaRepository<WarehouseJpaEntity, String> {
        List<WarehouseJpaEntity> findBySellerId(String sellerId);
    }

    @Repository
    public interface ProductSpringDataRepository extends JpaRepository<ProductJpaEntity, String> {
        List<ProductJpaEntity> findByStatus(String status);
        List<ProductJpaEntity> findBySellerId(String sellerId);
    }

    @Repository
    public interface InventorySpringDataRepository extends JpaRepository<InventoryJpaEntity, String> {
        Optional<InventoryJpaEntity> findByProductIdAndWarehouseId(String productId, String warehouseId);
        List<InventoryJpaEntity> findByProductId(String productId);
        List<InventoryJpaEntity> findByWarehouseId(String warehouseId);
    }

    @Repository
    public interface ShoppingCartSpringDataRepository extends JpaRepository<ShoppingCartJpaEntity, String> {
        Optional<ShoppingCartJpaEntity> findByBuyerId(String buyerId);
    }

    @Repository
    public interface OrderSpringDataRepository extends JpaRepository<OrderJpaEntity, String> {
        List<OrderJpaEntity> findByBuyerId(String buyerId);
    }

    @Repository
    public interface InvoiceSpringDataRepository extends JpaRepository<InvoiceJpaEntity, String> {
        Optional<InvoiceJpaEntity> findByOrderId(String orderId);
        Optional<InvoiceJpaEntity> findByInvoiceNumber(String invoiceNumber);
    }

    @Repository
    public interface ShipmentSpringDataRepository extends JpaRepository<ShipmentJpaEntity, String> {
        Optional<ShipmentJpaEntity> findByOrderId(String orderId);
        Optional<ShipmentJpaEntity> findByTrackingNumber(String trackingNumber);
    }

    @Repository
    public interface ReturnSpringDataRepository extends JpaRepository<ReturnJpaEntity, String> {
        List<ReturnJpaEntity> findByOrderId(String orderId);
    }

    @Repository
    public interface RefundSpringDataRepository extends JpaRepository<RefundJpaEntity, String> {
        Optional<RefundJpaEntity> findByReturnRequestId(String returnRequestId);
    }
}
