# NexusMarket - Implementation Progress and Architecture Log

## Overview
This document tracks the incremental progress, domain entity enhancements, exception hierarchy, output ports, domain services, and architectural design decisions for the **NexusMarket** platform, implemented following Domain-Driven Design (DDD) and Hexagonal Architecture (Ports & Adapters).

---

## 1. Task 1 - Review and Correction of Existing Domain Entities & Value Objects

### 1.1 Corrected Value Objects & Entities
1. **Warehouse & WarehouseType**:
   - **Problem**: `Warehouse.type` was modeled as a raw `String` (`"MARKETPLACE"` / `"SELLER"`).
   - **Correction**: Created `WarehouseType` value object extending `DomainCatalog`, providing type-safe singletons `WarehouseType.MARKETPLACE` and `WarehouseType.SELLER` with parsing helper `fromCode(String)`. Replaced the String field in `Warehouse` with `WarehouseType`.

2. **Product & ProductStatus**:
   - **Problem**: `Product` had only a primitive `boolean published` flag and lacked an explicit catalog lifecycle status.
   - **Correction**: Created `ProductStatus` value object extending `DomainCatalog` with states `PUBLISHED`, `SUSPENDED`, and `DISCONTINUED`. Added `publish()`, `suspend()`, and `discontinue()` domain methods, as well as `isPublished()`, `isSuspended()`, and `isDiscontinued()` query methods.

3. **Product Variants**:
   - **Problem**: Functional specification (OBJ-05, Domain 5) defines `variants` (list of color, size, model, etc.), but `Product` did not have this attribute.
   - **Correction**: Added `List<String> variants` and method `addVariant(String)` to `Product`.

4. **Monetary Values (BigDecimal vs Double)**:
   - **Problem**: Monetary prices and amounts were declared as `Double` in `Product` (`price`), `Order` (`totalAmount`), `Invoice` (`subtotal`, `taxes`, `total`), and `Refund` (`amount`).
   - **Correction**: Converted all monetary fields to `BigDecimal` to ensure precision, avoid floating-point rounding errors, and comply strictly with architectural standards.

5. **Order Lifecycle & Cancellation**:
   - **Problem**: `OrderStatus` lacked `CANCELLED` status needed for `cancelOrder` business process, and `Order` lacked cancellation logic.
   - **Correction**: Added `OrderStatus.CANCELLED`. Implemented `Order.cancel()`, validating that finalized and delivered orders cannot be cancelled. Added helper queries `isCancelled()`, `isPaid()`, `isDispatched()`, `isDelivered()`.

6. **Buyer Commercial Lifecycle**:
   - **Problem**: `Buyer` had `restrict()` but lacked `activate()` to restore commercial status to `ACTIVE`.
   - **Correction**: Added `activate()`, `isActive()`, `isRestricted()`, and `updateMainAddress(String)`.

7. **Inventory Damage Tracking & Consistency**:
   - **Problem**: Specification critical validation states that damaged inventory cannot be reserved, but `Inventory` lacked damage status. Exception messages were also in Spanish.
   - **Correction**: Added `damaged` boolean flag, `markAsDamaged()`, `markAsRepaired()`, and `isDamaged()`. Added validation in `reserve()` preventing reservation of damaged inventory. Translated all internal domain messages to 100% English.

8. **Invoice Voiding**:
   - **Problem**: Invoicing rules allow voiding an invoice (`anularFactura`), but `Invoice` lacked void state.
   - **Correction**: Added `voided` boolean flag, `voidInvoice()`, and `isVoided()`.

9. **Refund Processing**:
   - **Problem**: `Refund` lacked state tracking for whether a refund was already processed.
   - **Correction**: Added `processed` boolean flag, `process()`, and `isProcessed()`.

10. **User Logical Deletion & State Checks**:
    - **Problem**: `User` had no logical deletion capability and lacked helper methods to check blocked/inactive status.
    - **Correction**: Added `deleted` boolean flag, `deactivate()`, `delete()`, `isDeleted()`, `isBlocked()`, `isInactive()`.

11. **Value Objects Enhancement**:
    - Added `fromCode(String code)` factory methods to all `DomainCatalog` subclasses (`UserRole`, `UserStatus`, `CommercialStatus`, `ProductType`, `ProductStatus`, `OrderStatus`, `PaymentStatus`, `ReturnStatus`, `ShipmentStatus`, `InventoryMovementType`, `WarehouseType`) for safe lookups from incoming strings without using raw strings in the domain model.

---

## 2. Task 2 & 3 - Domain Exceptions, Ports, and Domain Services

### 2.1 Common & Cross-Cutting Exceptions (`application.domain.exceptions.common`)
- **`DomainException`**: Abstract base runtime exception for all domain violations across NexusMarket.
- **`InvalidDataException`**: Thrown when provided inputs/payloads fail domain boundary validations (null, empty, negative, etc.).
- **`DuplicateEntityException`**: Base exception thrown when an entity already exists with the given unique identifier.
- **`UnauthenticatedUserException`**: Thrown when an operation lacks authenticated context (RG-01).
- **`SingleRoleException`**: Thrown when attempting to assign multiple roles to a user (RG-02).
- **`InsufficientPermissionException`**: Thrown when a user attempts an action outside their role privileges (RG-03, Responsibility Matrix).
- **`UnauthorizedOperationException`**: Thrown when an operation is prohibited by business state or security matrix.
- **`InvalidDateRangeException`**: Thrown when query or report date ranges are inverted or invalid.
- **`NoDataException`**: Thrown when a query or analytical report returns zero records.

---

### 2.2 User Domain (`application.domain.services.user`, `application.domain.exceptions.user`)
- **Exceptions Created**:
  - `UserNotFoundException`: Raised when looking up a non-existent user identifier.
  - `DuplicateEmailException`: Raised when attempting to register or update an email that already exists in the system.
  - `DuplicateIdentificationException`: Raised when a user ID/identification document is already registered.
  - `InactiveUserException`: Raised when an inactive or blocked user attempts restricted operations.
  - `InvalidRoleException`: Raised when role assignment is missing, invalid, or violates RG-02.
- **Output Port**:
  - `UserRepositoryPort`: Outbound interface abstracting persistence operations (`save`, `findById`, `findByEmail`, `existsById`, `existsByEmail`, `findAll`, `deleteById`).
- **Domain Service**:
  - `UserService`: Implements OBJ-01 (User Administration).
    - `createUser(User)`: Validates mandatory attributes, ID and email uniqueness, role assignment (RG-02), and persists the user.
    - `getUserById(String)`: Enforces non-blank validation and entity existence check.
    - `getAllUsers()`: Retrieves all registered users.
    - `updateUser(String, String, String)`: Allows modification of name and email (with conflict checking), while keeping the user role immutable.
    - `blockUser(String)`: Invokes domain behavior `user.block()` and saves.
    - `activateUser(String)`: Invokes domain behavior `user.activate()` and saves.
    - `deleteUser(String)`: Executes logical deletion via `user.delete()` and saves.

---

### 2.3 Buyer Domain (`application.domain.services.buyer`, `application.domain.exceptions.buyer`)
- **Exceptions Created**:
  - `BuyerNotFoundException`: Raised when searching for a non-existent buyer.
  - `RestrictedBuyerException`: Raised when a buyer with RESTRICTED commercial status attempts unauthorized purchases.
  - `InvalidAddressException`: Raised when provided delivery addresses are blank or invalid.
- **Output Port**:
  - `BuyerRepositoryPort`: Interface for buyer persistence operations (`save`, `findById`, `findByUserId`, `existsById`, `existsByUserId`, `findAll`).
- **Domain Service**:
  - `BuyerService`: Implements OBJ-03 (Buyer Management).
    - `registerBuyer(String id, String userId, String mainAddress)`: Validates that the associated user exists, is active, and possesses the `BUYER` role exclusively. Enforces unique buyer profile per user.
    - `getBuyerById(String)`: Retrieves a buyer by ID with existence validation.
    - `getAllBuyers()`: Retrieves all buyers.
    - `addAdditionalAddress(String, String)`: Adds a non-blank secondary delivery address.
    - `updateMainAddress(String, String)`: Updates the primary delivery address.
    - `restrictBuyer(String)`: Transitions commercial status to `RESTRICTED`.
    - `activateBuyer(String)`: Restores commercial status to `ACTIVE`.

---

### 2.4 Seller Domain (`application.domain.services.seller`, `application.domain.exceptions.seller`)
- **Exceptions Created**:
  - `SellerNotFoundException`: Raised when looking up a non-existent seller.
  - `SelfRegistrationNotAllowedException`: Enforces the business rule that sellers cannot self-register.
  - `SellerWithoutWarehouseException`: Raised when an operation requires an assigned warehouse but seller has none.
  - `WarehouseAlreadyAssignedException`: Raised when attempting to assign a warehouse already linked to another seller.
  - `WarehouseNotFoundException`: Raised when the target warehouse for assignment is not found.
- **Output Ports**:
  - `SellerRepositoryPort`: Outbound interface for seller storage operations (`save`, `findById`, `findByUserId`, `findByTaxId`, `existsById`, `existsByUserId`, `existsByTaxId`, `findAll`).
  - `WarehouseRepositoryPort`: Outbound interface for warehouse storage operations (`save`, `findById`, `findAll`, `findBySellerId`, `existsById`).
- **Domain Service**:
  - `SellerService`: Implements OBJ-02 (Seller Management).
    - `registerSeller(String id, String userId, String taxId, String adminUserId)`: Strictly requires an authenticated `ADMIN` user to register sellers. Blocks self-registration attempts. Validates tax ID uniqueness and associated user status/role.
    - `getSellerById(String)`: Retrieves a seller with existence validation.
    - `getAllSellers()`: Lists all registered sellers.
    - `updateSeller(String, String)`: Updates tax ID while ensuring tax ID uniqueness.
    - `assignWarehouse(String, String)`: Links a warehouse to a seller and marks the warehouse type as `SELLER`, preventing conflicting assignments.
    - `validateSellerHasWarehouses(String)`: Validates that a seller has at least one associated warehouse before allowing product publishing or inventory intake.

---

### 2.5 Warehouse Domain (`application.domain.services.warehouse`, `application.domain.exceptions.warehouse`)
- **Exceptions Created**:
  - `WarehouseNotFoundException`: Raised when looking up a warehouse by identifier that does not exist.
  - `InvalidWarehouseTypeException`: Raised when an invalid or null warehouse type is provided.
  - `WarehouseAlreadyAssignedException`: Raised when attempting to assign a warehouse already belonging to another seller.
- **Output Port**:
  - `WarehouseRepositoryPort`: Outbound interface managing warehouse queries and persistence (`save`, `findById`, `findAll`, `findBySellerId`, `existsById`).
- **Domain Service**:
  - `WarehouseService`: Implements OBJ-04 (Warehouse Management).
    - `createWarehouse(String id, String name, String address, WarehouseType type, String sellerId)`: Enforces non-blank address and name, validates type, ensures unique ID, associates with seller if `SELLER` type, and updates the seller's warehouse list.
    - `getWarehouseById(String)`: Retrieves warehouse by ID.
    - `getAllWarehouses()`: Lists all warehouses across marketplace and sellers.
    - `updateWarehouse(String, String, String)`: Updates warehouse metadata (name, physical address).
    - `listWarehousesBySeller(String)`: Retrieves warehouses assigned to a specific verified seller.

---

### 2.6 Catalog & Product Domain (`application.domain.services.product`, `application.domain.exceptions.product`)
- **Exceptions Created**:
  - `ProductNotFoundException`: Raised when searching for a non-existent product ID.
  - `ProductNotPublishedException`: Raised when trying to access or order an unpublished product.
  - `ProductAlreadyPublishedException`: Raised when attempting to publish a product that is already in `PUBLISHED` status.
  - `InvalidPriceException`: Raised when product price is negative or zero (violating positive price rule).
  - `InvalidProductTypeException`: Raised when product type is missing or not recognized.
- **Output Port**:
  - `ProductRepositoryPort`: Outbound interface managing product persistence (`save`, `findById`, `findAll`, `findByStatus`, `findBySellerId`, `existsById`, `deleteById`).
- **Domain Service**:
  - `CatalogService`: Implements OBJ-05 (Catalog Management).
    - `registerProduct(...)`: Enforces price > 0 (BigDecimal), validates product type, ensures existence of seller, and validates that sellers of physical products have at least one registered warehouse before registering products.
    - `getProductById(String)`: Retrieves product by ID.
    - `listPublishedProducts()`: Returns all products visible to buyers (`ProductStatus.PUBLISHED`).
    - `listProductsBySeller(String)`: Returns all products offered by a given seller.
    - `updateProduct(...)`: Allows modification of descriptive fields, price (revalidating positivity), and variants.
    - `publishProduct(String)`: Changes status from `SUSPENDED` to `PUBLISHED` with physical product warehouse checks.
    - `hideProduct(String)`: Temporarily suspends the product from public view.
    - `discontinueProduct(String)`: Permanently discontinues the product.
    - `deleteProduct(String)`: Executes logical removal of the product from the catalog.

---
