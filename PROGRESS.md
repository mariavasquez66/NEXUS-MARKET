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
