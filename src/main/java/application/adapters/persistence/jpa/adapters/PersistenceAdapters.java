package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.*;
import application.adapters.persistence.jpa.mappers.JpaEntityMappers;
import application.adapters.persistence.jpa.repositories.Repositories.*;
import application.domain.entities.*;
import application.domain.ports.out.*;
import application.domain.valueobjects.ProductStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PersistenceAdapters {

    @Component
    public static class UserJpaAdapter implements UserRepositoryPort {
        private final UserSpringDataRepository repository;

        public UserJpaAdapter(UserSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public User save(User user) {
            UserJpaEntity entity = JpaEntityMappers.toJpaEntity(user);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<User> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return repository.findByEmail(email).map(JpaEntityMappers::toDomain);
        }

        @Override
        public boolean existsById(String id) {
            return repository.existsById(id);
        }

        @Override
        public boolean existsByEmail(String email) {
            return repository.existsByEmail(email);
        }

        @Override
        public List<User> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public void deleteById(String id) {
            repository.deleteById(id);
        }
    }

    @Component
    public static class BuyerJpaAdapter implements BuyerRepositoryPort {
        private final BuyerSpringDataRepository repository;

        public BuyerJpaAdapter(BuyerSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Buyer save(Buyer buyer) {
            BuyerJpaEntity entity = JpaEntityMappers.toJpaEntity(buyer);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Buyer> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<Buyer> findByUserId(String userId) {
            return repository.findByUserId(userId).map(JpaEntityMappers::toDomain);
        }

        @Override
        public boolean existsById(String id) {
            return repository.existsById(id);
        }

        @Override
        public boolean existsByUserId(String userId) {
            return repository.existsByUserId(userId);
        }

        @Override
        public List<Buyer> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }
    }

    @Component
    public static class SellerJpaAdapter implements SellerRepositoryPort {
        private final SellerSpringDataRepository sellerRepo;
        private final WarehouseSpringDataRepository warehouseRepo;

        public SellerJpaAdapter(SellerSpringDataRepository sellerRepo, WarehouseSpringDataRepository warehouseRepo) {
            this.sellerRepo = sellerRepo;
            this.warehouseRepo = warehouseRepo;
        }

        @Override
        public Seller save(Seller seller) {
            SellerJpaEntity entity = JpaEntityMappers.toJpaEntity(seller);
            SellerJpaEntity saved = sellerRepo.save(entity);
            return JpaEntityMappers.toDomain(saved, seller.getWarehouses());
        }

        @Override
        public Optional<Seller> findById(String id) {
            return sellerRepo.findById(id).map(s -> {
                List<Warehouse> warehouses = warehouseRepo.findBySellerId(s.getId()).stream()
                        .map(JpaEntityMappers::toDomain)
                        .collect(Collectors.toList());
                return JpaEntityMappers.toDomain(s, warehouses);
            });
        }

        @Override
        public Optional<Seller> findByUserId(String userId) {
            return sellerRepo.findByUserId(userId).map(s -> {
                List<Warehouse> warehouses = warehouseRepo.findBySellerId(s.getId()).stream()
                        .map(JpaEntityMappers::toDomain)
                        .collect(Collectors.toList());
                return JpaEntityMappers.toDomain(s, warehouses);
            });
        }

        @Override
        public Optional<Seller> findByTaxId(String taxId) {
            return sellerRepo.findByTaxId(taxId).map(s -> {
                List<Warehouse> warehouses = warehouseRepo.findBySellerId(s.getId()).stream()
                        .map(JpaEntityMappers::toDomain)
                        .collect(Collectors.toList());
                return JpaEntityMappers.toDomain(s, warehouses);
            });
        }

        @Override
        public boolean existsById(String id) {
            return sellerRepo.existsById(id);
        }

        @Override
        public boolean existsByUserId(String userId) {
            return sellerRepo.existsByUserId(userId);
        }

        @Override
        public boolean existsByTaxId(String taxId) {
            return sellerRepo.existsByTaxId(taxId);
        }

        @Override
        public List<Seller> findAll() {
            return sellerRepo.findAll().stream().map(s -> {
                List<Warehouse> warehouses = warehouseRepo.findBySellerId(s.getId()).stream()
                        .map(JpaEntityMappers::toDomain)
                        .collect(Collectors.toList());
                return JpaEntityMappers.toDomain(s, warehouses);
            }).collect(Collectors.toList());
        }
    }

    @Component
    public static class WarehouseJpaAdapter implements WarehouseRepositoryPort {
        private final WarehouseSpringDataRepository repository;

        public WarehouseJpaAdapter(WarehouseSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Warehouse save(Warehouse warehouse) {
            WarehouseJpaEntity entity = JpaEntityMappers.toJpaEntity(warehouse);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Warehouse> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public List<Warehouse> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public List<Warehouse> findBySellerId(String sellerId) {
            return repository.findBySellerId(sellerId).stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public boolean existsById(String id) {
            return repository.existsById(id);
        }
    }

    @Component
    public static class ProductJpaAdapter implements ProductRepositoryPort {
        private final ProductSpringDataRepository repository;

        public ProductJpaAdapter(ProductSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Product save(Product product) {
            ProductJpaEntity entity = JpaEntityMappers.toJpaEntity(product);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Product> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public List<Product> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public List<Product> findByStatus(ProductStatus status) {
            return repository.findByStatus(status.getCode()).stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public List<Product> findBySellerId(String sellerId) {
            return repository.findBySellerId(sellerId).stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public boolean existsById(String id) {
            return repository.existsById(id);
        }

        @Override
        public void deleteById(String id) {
            repository.deleteById(id);
        }
    }

    @Component
    public static class InventoryJpaAdapter implements InventoryRepositoryPort {
        private final InventorySpringDataRepository repository;

        public InventoryJpaAdapter(InventorySpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Inventory save(Inventory inventory) {
            InventoryJpaEntity entity = JpaEntityMappers.toJpaEntity(inventory);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Inventory> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<Inventory> findByProductIdAndWarehouseId(String productId, String warehouseId) {
            return repository.findByProductIdAndWarehouseId(productId, warehouseId).map(JpaEntityMappers::toDomain);
        }

        @Override
        public List<Inventory> findByProductId(String productId) {
            return repository.findByProductId(productId).stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public List<Inventory> findByWarehouseId(String warehouseId) {
            return repository.findByWarehouseId(warehouseId).stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public boolean existsById(String id) {
            return repository.existsById(id);
        }

        @Override
        public List<Inventory> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }
    }

    @Component
    public static class ShoppingCartJpaAdapter implements ShoppingCartRepositoryPort {
        private final ShoppingCartSpringDataRepository repository;

        public ShoppingCartJpaAdapter(ShoppingCartSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public ShoppingCart save(ShoppingCart cart) {
            ShoppingCartJpaEntity entity = JpaEntityMappers.toJpaEntity(cart);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<ShoppingCart> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<ShoppingCart> findByBuyerId(String buyerId) {
            return repository.findByBuyerId(buyerId).map(JpaEntityMappers::toDomain);
        }

        @Override
        public void deleteById(String id) {
            repository.deleteById(id);
        }
    }

    @Component
    public static class OrderJpaAdapter implements OrderRepositoryPort {
        private final OrderSpringDataRepository repository;

        public OrderJpaAdapter(OrderSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Order save(Order order) {
            OrderJpaEntity entity = JpaEntityMappers.toJpaEntity(order);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Order> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public List<Order> findByBuyerId(String buyerId) {
            return repository.findByBuyerId(buyerId).stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public List<Order> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public boolean existsById(String id) {
            return repository.existsById(id);
        }
    }

    @Component
    public static class InvoiceJpaAdapter implements InvoiceRepositoryPort {
        private final InvoiceSpringDataRepository repository;

        public InvoiceJpaAdapter(InvoiceSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Invoice save(Invoice invoice) {
            InvoiceJpaEntity entity = JpaEntityMappers.toJpaEntity(invoice);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Invoice> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<Invoice> findByOrderId(String orderId) {
            return repository.findByOrderId(orderId).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
            return repository.findByInvoiceNumber(invoiceNumber).map(JpaEntityMappers::toDomain);
        }
    }

    @Component
    public static class ShipmentJpaAdapter implements ShipmentRepositoryPort {
        private final ShipmentSpringDataRepository repository;

        public ShipmentJpaAdapter(ShipmentSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Shipment save(Shipment shipment) {
            ShipmentJpaEntity entity = JpaEntityMappers.toJpaEntity(shipment);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Shipment> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<Shipment> findByOrderId(String orderId) {
            return repository.findByOrderId(orderId).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<Shipment> findByTrackingNumber(String trackingNumber) {
            return repository.findByTrackingNumber(trackingNumber).map(JpaEntityMappers::toDomain);
        }

        @Override
        public List<Shipment> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }
    }

    @Component
    public static class ReturnJpaAdapter implements ReturnRepositoryPort {
        private final ReturnSpringDataRepository repository;

        public ReturnJpaAdapter(ReturnSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Return save(Return returnRequest) {
            ReturnJpaEntity entity = JpaEntityMappers.toJpaEntity(returnRequest);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Return> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public List<Return> findByOrderId(String orderId) {
            return repository.findByOrderId(orderId).stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }

        @Override
        public List<Return> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }
    }

    @Component
    public static class RefundJpaAdapter implements RefundRepositoryPort {
        private final RefundSpringDataRepository repository;

        public RefundJpaAdapter(RefundSpringDataRepository repository) {
            this.repository = repository;
        }

        @Override
        public Refund save(Refund refund) {
            RefundJpaEntity entity = JpaEntityMappers.toJpaEntity(refund);
            return JpaEntityMappers.toDomain(repository.save(entity));
        }

        @Override
        public Optional<Refund> findById(String id) {
            return repository.findById(id).map(JpaEntityMappers::toDomain);
        }

        @Override
        public Optional<Refund> findByReturnRequestId(String returnRequestId) {
            return repository.findByReturnRequestId(returnRequestId).map(JpaEntityMappers::toDomain);
        }

        @Override
        public List<Refund> findAll() {
            return repository.findAll().stream().map(JpaEntityMappers::toDomain).collect(Collectors.toList());
        }
    }
}
