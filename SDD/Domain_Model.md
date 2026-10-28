# NexusMarket - Software Design Document (SDD): Domain Model

## 1. Introduction and Architectural Overview

This document specifies the core Domain Model for the **NexusMarket** digital commerce platform following **Domain-Driven Design (DDD)** and **Hexagonal Architecture (Ports and Adapters)**.

All domain concepts, entities, value objects, domain events, output ports, and services are purely implemented in Java, residing within `application.domain.*` (or `domain.*`), completely decoupled from UI frameworks, persistence mechanisms (JPA, MongoDB), serialization (Jackson), or web transport libraries.

---

## 2. Package Architecture

```text
application/domain/
├── entities/
│   ├── User.java                   // Aggregate Root (User identity & role control)
│   ├── Buyer.java                  // Commercial buyer profile & delivery addresses
│   ├── Seller.java                 // Vendor profile & linked warehouses
│   ├── Warehouse.java              // Aggregate Root (Physical fulfillment storage)
│   ├── Product.java                // Aggregate Root (Catalog item & variants)
│   ├── Inventory.java              // Aggregate Root (Distributed warehouse stock)
│   ├── ShoppingCart.java           // Active customer cart & items
│   ├── Order.java                  // Aggregate Root (Order lifecycle state machine)
│   ├── Invoice.java                // Financial billing transaction
│   ├── Shipment.java               // Physical order logistics dispatch
│   ├── Return.java                 // Return merchandise request
│   └── Refund.java                 // Financial reimbursement transaction
│
├── valueobjects/
│   ├── DomainCatalog.java          // Base abstract class for immutable domain catalogs
│   ├── UserRole.java               // BUYER, SELLER, LOGISTIC_OPERATOR, ADMIN, SUPERVISOR
│   ├── UserStatus.java             // ACTIVE, BLOCKED, INACTIVE
│   ├── CommercialStatus.java       // ACTIVE, RESTRICTED
│   ├── ProductType.java            // PHYSICAL, DIGITAL
│   ├── ProductStatus.java          // PUBLISHED, SUSPENDED, DISCONTINUED
│   ├── WarehouseType.java          // MARKETPLACE, SELLER
│   ├── InventoryMovementType.java  // INFLOW, RESERVATION, SALE, ADJUSTMENT, RETURN
│   ├── OrderStatus.java            // CART, PENDING_PAYMENT, PAID, SHIPPED, DELIVERED, CANCELLED
│   ├── PaymentStatus.java          // PENDING, CONFIRMED, REJECTED
│   ├── ShipmentStatus.java         // PENDING, IN_TRANSIT, DELIVERED
│   └── ReturnStatus.java           // REQUESTED, APPROVED, REJECTED, COMPLETED
│
├── exceptions/
│   ├── common/                     // DomainException, DuplicateEntityException, etc.
│   ├── user/                       // UserNotFoundException, DuplicateEmailException, etc.
│   ├── buyer/                      // BuyerNotFoundException, RestrictedBuyerException, etc.
│   ├── seller/                     // SellerNotFoundException, SelfRegistrationNotAllowedException
│   ├── warehouse/                  // WarehouseNotFoundException, InvalidWarehouseTypeException
│   ├── product/                    // ProductNotFoundException, InvalidPriceException, etc.
│   ├── inventory/                  // InsufficientStockException, NegativeStockException, etc.
│   └── cart/                       // CartNotFoundException
│
├── ports/
│   ├── in/                         // Role-based driving input ports (Use Case contracts)
│   └── out/                        // Driven output ports (Repository & infrastructure SPIs)
│
└── services/
    ├── user/                       // UserService
    ├── buyer/                      // BuyerService
    ├── seller/                     // SellerService
    ├── warehouse/                  // WarehouseService
    ├── product/                    // CatalogService
    └── inventory/                  // InventoryService
```

---

## 3. Value Objects and Catalog Enums

All catalog value objects extend the base immutable class `DomainCatalog` or enforce immutable encapsulation.

### 3.1 Base Class: `DomainCatalog`
- **Fields**: `code: String`, `name: String`, `description: String`.
- **Invariants**: Immutable; identity based strictly on `code`.

### 3.2 Value Objects Matrix
| Value Object | Allowed Codes / Values | Invariants & Semantics |
| :--- | :--- | :--- |
| `UserRole` | `BUYER`, `SELLER`, `LOGISTIC_OPERATOR`, `ADMIN`, `SUPERVISOR` | RG-02: Exactly one role per user. Immutable. |
| `UserStatus` | `ACTIVE`, `BLOCKED`, `INACTIVE` | Only active users can perform write operations. |
| `CommercialStatus` | `ACTIVE`, `RESTRICTED` | Restricted buyers cannot place new orders. |
| `ProductType` | `PHYSICAL`, `DIGITAL` | Physical requires distributed inventory & shipment. Digital enables immediate fulfillment. |
| `ProductStatus` | `PUBLISHED`, `SUSPENDED`, `DISCONTINUED` | Only published products appear in public search and can be purchased. |
| `WarehouseType` | `MARKETPLACE`, `SELLER` | Seller warehouses must be linked to an approved seller. |
| `InventoryMovementType` | `INFLOW`, `RESERVATION`, `SALE`, `ADJUSTMENT`, `RETURN` | Full traceability for stock audit logs. |
| `OrderStatus` | `CART`, `PENDING_PAYMENT`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELLED` | Deterministic state machine. Finalized/delivered orders are immutable. |
| `PaymentStatus` | `PENDING`, `CONFIRMED`, `REJECTED` | Transitions driven by payment gateway responses. |
| `ShipmentStatus` | `PENDING`, `IN_TRANSIT`, `DELIVERED` | Tracked by logistics operator. |
| `ReturnStatus` | `REQUESTED`, `APPROVED`, `REJECTED`, `COMPLETED` | Evaluated by admin/operator before refund dispatch. |

---

## 4. Domain Entities and Aggregates

### 4.1 `User` (Aggregate Root)
- **Identity**: `id: String` (immutable unique identifier).
- **Attributes**:
  - `fullName: String`
  - `email: String` (unique in the system)
  - `role: UserRole` (single role, immutable)
  - `status: UserStatus`
  - `deleted: boolean` (logical deletion)
- **Domain Methods**:
  - `block()`: Sets status to `BLOCKED`.
  - `activate()`: Restores status to `ACTIVE`.
  - `deactivate()`: Sets status to `INACTIVE`.
  - `delete()`: Marks as logically deleted and deactivates.
  - `updateProfile(fullName, email)`: Updates non-identity data.
  - `isActive()`, `isBlocked()`, `isInactive()`, `isDeleted()`: Predicate queries.

### 4.2 `Buyer`
- **Identity**: `id: String`.
- **Attributes**:
  - `user: User` (associated user with role `BUYER`)
  - `mainAddress: String` (mandatory primary delivery address)
  - `additionalAddresses: List<String>` (secondary locations)
  - `commercialStatus: CommercialStatus`
- **Domain Methods**:
  - `updateMainAddress(address)`: Validates and updates primary address.
  - `addAdditionalAddress(address)`: Appends validated secondary address.
  - `restrict()`: Sets commercial status to `RESTRICTED`.
  - `activate()`: Restores commercial status to `ACTIVE`.
  - `isActive()`, `isRestricted()`: Predicate queries.

### 4.3 `Seller`
- **Identity**: `id: String`.
- **Attributes**:
  - `user: User` (associated user with role `SELLER`)
  - `taxId: String` (NIT / Tax ID, unique)
  - `warehouses: List<Warehouse>` (linked storage facilities)
- **Domain Methods**:
  - `addWarehouse(Warehouse)`: Links a warehouse and updates warehouse ownership.
  - `updateTaxId(taxId)`: Updates fiscal registration identifier.
  - `hasWarehouses()`: Verifies if seller has at least one active warehouse.

### 4.4 `Warehouse` (Aggregate Root)
- **Identity**: `id: String`.
- **Attributes**:
  - `name: String`
  - `address: String`
  - `type: WarehouseType` (`MARKETPLACE` or `SELLER`)
  - `seller: Seller` (null if marketplace-owned)
- **Domain Methods**:
  - `assignSeller(Seller)`: Links to seller and enforces `type = WarehouseType.SELLER`.
  - `updateDetails(name, address)`: Updates warehouse metadata.
  - `isSellerWarehouse()`, `isMarketplaceWarehouse()`: Predicate queries.

### 4.5 `Product` (Aggregate Root)
- **Identity**: `id: String`.
- **Attributes**:
  - `name: String`
  - `description: String`
  - `price: BigDecimal` (strictly positive, non-zero)
  - `type: ProductType` (`PHYSICAL`, `DIGITAL`)
  - `status: ProductStatus` (`PUBLISHED`, `SUSPENDED`, `DISCONTINUED`)
  - `seller: Seller`
  - `variants: List<String>` (color, size, model, etc.)
- **Domain Methods**:
  - `publish()`: Validates and transitions status to `PUBLISHED`.
  - `suspend()`: Transitions status to `SUSPENDED`.
  - `discontinue()`: Transitions status to `DISCONTINUED`.
  - `updatePrice(BigDecimal)`: Enforces positive value.
  - `addVariant(String)`: Adds product variant.
  - `isPublished()`, `isSuspended()`, `isDiscontinued()`: Predicate queries.

### 4.6 `Inventory` (Aggregate Root)
- **Identity**: `id: String`.
- **Attributes**:
  - `product: Product`
  - `warehouse: Warehouse`
  - `availableQuantity: Integer` (strictly $\ge 0$)
  - `reservedQuantity: Integer` (strictly $\ge 0$)
  - `damaged: boolean` (damage tracking)
- **Domain Methods & Invariants**:
  - **Negative Stock Prohibition**: `availableQuantity` and `reservedQuantity` can never be negative.
  - `addStock(quantity)`: Increases available quantity.
  - `reserve(quantity)`: Checks that inventory is not damaged and available quantity $\ge$ requested; moves available to reserved.
  - `releaseReservation(quantity)`: Moves reserved quantity back to available.
  - `confirmSale(quantity)`: Permanently deducts from reserved stock upon paid order.
  - `markAsDamaged()`, `markAsRepaired()`: Controls damage state.
  - `isDamaged()`: Invariant check preventing sales or reservations.

### 4.7 `ShoppingCart` & Cart Items
- **Identity**: `id: String`.
- **Attributes**:
  - `buyer: Buyer`
  - `items: List<CartItem>`
- **Domain Methods**:
  - `addItem(productId, quantity, unitPrice)`
  - `removeItem(productId)`
  - `clear()`
  - `calculateTotal(): BigDecimal`

### 4.8 `Order` (Aggregate Root)
- **Identity**: `id: String`.
- **Attributes**:
  - `buyer: Buyer`
  - `items: List<CartItem>`
  - `totalAmount: BigDecimal`
  - `status: OrderStatus`
  - `paymentStatus: PaymentStatus`
  - `createdAt: LocalDateTime`
  - `paidAt: LocalDateTime`
  - `dispatchedAt: LocalDateTime`
  - `deliveredAt: LocalDateTime`
  - `cancelledAt: LocalDateTime`
- **Domain Invariants & Methods**:
  - **Immutability Rule**: Orders in `DELIVERED` or `CANCELLED` status cannot be altered.
  - `markAsPaid()`: Transitions to `PAID` and sets `paidAt`.
  - `dispatch()`: Transitions to `SHIPPED` and sets `dispatchedAt`.
  - `deliver()`: Transitions to `DELIVERED` and sets `deliveredAt`.
  - `cancel()`: Validates order is not delivered or already finalized; sets to `CANCELLED`.
  - `isFinalized()`, `isCancelled()`, `isPaid()`, `isDispatched()`, `isDelivered()`: Predicate queries.

### 4.9 `Invoice`, `Shipment`, `Return`, `Refund`
- **`Invoice`**: `id`, `order`, `invoiceNumber`, `subtotal: BigDecimal`, `taxes: BigDecimal`, `total: BigDecimal`, `issueDate`, `voided: boolean`. Supports `voidInvoice()`.
- **`Shipment`**: `id`, `order`, `trackingNumber`, `originWarehouse`, `destinationAddress`, `status: ShipmentStatus`, `dispatchDate`, `deliveryDate`.
- **`Return`**: `id`, `order`, `reason`, `status: ReturnStatus`, `requestDate`, `resolutionDate`.
- **`Refund`**: `id`, `returnRequest`, `amount: BigDecimal`, `paymentMethod`, `processed: boolean`. Supports `process()`.

---

## 5. Domain Invariants and Critical Rules Summary

1. **User Identity & Role (RG-01, RG-02)**: Unique email and identification per user. Exactly one role per user.
2. **Seller Self-Registration Prohibition**: Only `ADMIN` users can register a seller profile.
3. **Physical Goods Fulfillment Invariant**: A seller offering physical products must have at least one registered warehouse.
4. **Non-Negative Inventory Invariant**: Stock quantities (`available` and `reserved`) must be $\ge 0$ at all times.
5. **Damaged Stock Invariant**: Inventory flagged as `damaged = true` cannot be reserved or sold.
6. **Order Immutability**: Any order in terminal state (`DELIVERED` / `CANCELLED`) is completely immutable.
7. **Monetary Precision**: All monetary attributes are represented strictly using `BigDecimal`.
8. **100% English Code Standard**: All identifiers, class names, method names, enums, exception classes, and log messages in Java are in English.

