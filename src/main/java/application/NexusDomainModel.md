# NEXUSMARKET - MODELO DE DOMINIO

## Introducción

Este documento describe el modelo de dominio de la plataforma **NexusMarket**, basado en la Especificación Funcional del Negocio. El modelo captura las entidades principales, sus atributos, comportamientos y relaciones, así como las reglas de negocio y restricciones críticas.

El diseño sigue los principios de **Domain-Driven Design (DDD)** y se alinea con la estructura del repositorio de ejemplo proporcionado.

## Estructura de Paquetes

application/
├── domain/
│ ├── entities/
│ │ ├── Usuario.java
│ │ ├── Comprador.java
│ │ ├── Vendedor.java
│ │ ├── Bodega.java
│ │ ├── Producto.java
│ │ ├── Inventario.java
│ │ ├── CarritoDeCompras.java
│ │ ├── Pedido.java
│ │ ├── Factura.java
│ │ ├── Envio.java
│ │ ├── Devolucion.java
│ │ └── Reembolso.java
│ └── valueobjects/
│ ├── CatalogoDominio.java
│ ├── RolDeUsuario.java
│ ├── EstadoDeUsuario.java
│ ├── EstadoComercial.java
│ ├── TipoDeProducto.java
│ ├── TipoMovimientoInventario.java
│ ├── EstadoDePedido.java
│ ├── EstadoDePago.java
│ ├── EstadoDeEnvio.java
│ └── EstadoDeDevolucion.java


---

## Objetos de Valor (Value Objects)

Todos los objetos de valor extienden de la clase base `CatalogoDominio` y son inmutables.

### `CatalogoDominio.java` (Clase Base)

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `codigo` | `String` | Identificador único del catálogo. |
| `nombre` | `String` | Nombre descriptivo. |
| `descripcion` | `String` | Descripción funcional. |

**Métodos**: Getters y `equals/hashCode` basados en `codigo`.

---

### `RolDeUsuario.java`

**Valores definidos**:
- `COMPRADOR`
- `VENDEDOR`
- `OPERADOR_LOGISTICO`
- `ADMINISTRADOR`
- `SUPERVISOR`

---

### `EstadoDeUsuario.java`

**Valores definidos**:
- `ACTIVO`
- `BLOQUEADO`
- `INACTIVO`

---

### `EstadoComercial.java`

**Valores definidos**:
- `ACTIVO`
- `RESTRINGIDO`

---

### `TipoDeProducto.java`

**Valores definidos**:
- `FISICO`
- `DIGITAL`

---

### `TipoMovimientoInventario.java`

**Valores definidos**:
- `INGRESO`
- `RESERVA`
- `SALIDA_POR_VENTA`
- `AJUSTE`
- `DEVOLUCION`

---

### `EstadoDePedido.java`

**Valores definidos**:
- `CARRITO`
- `PENDIENTE_PAGO`
- `PAGADO`
- `DESPACHADO`
- `ENTREGADO`
- `FINALIZADO`

---

### `EstadoDePago.java`

**Valores definidos**:
- `PENDIENTE`
- `CONFIRMADO`
- `RECHAZADO`

---

### `EstadoDeEnvio.java`

**Valores definidos**:
- `PENDIENTE`
- `EN_TRANSITO`
- `ENTREGADO`

---

### `EstadoDeDevolucion.java`

**Valores definidos**:
- `SOLICITADO`
- `APROBADO`
- `RECHAZADO`
- `COMPLETADO`

---

## Entidades del Dominio

A continuación se describen las entidades con sus atributos principales y métodos de negocio.

---

### `Usuario.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador único. |
| `nombreCompleto` | `String` | Nombre oficial. |
| `correoElectronico` | `String` | Medio de acceso y comunicación. |
| `rol` | `RolDeUsuario` | Rol único del usuario. |
| `estado` | `EstadoDeUsuario` | Condición operativa. |

**Métodos**:
- `bloquear()`: Cambia estado a `BLOQUEADO`.
- `activar()`: Cambia estado a `ACTIVO`.
- `estaActivo()`: Devuelve `true` si estado es `ACTIVO`.

---

### `Comprador.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `usuario` | `Usuario` | Usuario asociado. |
| `direccionPrincipal` | `String` | Ubicación habitual para entregas. |
| `direccionesAdicionales` | `List<String>` | Otras ubicaciones de entrega. |
| `estadoComercial` | `EstadoComercial` | Condición para realizar compras. |

**Métodos**:
- `agregarDireccion(String direccion)`
- `restringir()`: Cambia estado a `RESTRINGIDO`.

---

### `Vendedor.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `usuario` | `Usuario` | Usuario asociado. |
| `identificacionTributaria` | `String` | NIT / RUC. |
| `bodegas` | `List<Bodega>` | Bodegas asociadas. |

**Métodos**:
- `agregarBodega(Bodega bodega)`

---

### `Bodega.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `nombre` | `String` | Nombre de la bodega. |
| `direccion` | `String` | Ubicación física. |
| `tipo` | `String` | `MARKETPLACE` o `VENDEDOR`. |
| `vendedor` | `Vendedor` | Vendedor asociado (null si es del Marketplace). |

---

### `Producto.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `nombre` | `String` | Nombre del producto. |
| `descripcion` | `String` | Descripción. |
| `precio` | `Double` | Precio unitario. |
| `tipo` | `TipoDeProducto` | `FISICO` o `DIGITAL`. |
| `vendedor` | `Vendedor` | Vendedor que lo ofrece. |
| `publicado` | `boolean` | Indica si es visible en el catálogo. |

**Métodos**:
- `publicar()`: Cambia `publicado` a `true`.
- `ocultar()`: Cambia `publicado` a `false`.

---

### `Inventario.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `producto` | `Producto` | Producto asociado. |
| `bodega` | `Bodega` | Bodega donde se almacena. |
| `cantidad` | `int` | Cantidad total en stock. |
| `cantidadReservada` | `int` | Cantidad apartada para pedidos en proceso. |

**Métodos**:
- `agregarStock(int cantidad)`
- `reservar(int cantidad)`: Lanza excepción si no hay suficiente disponible.
- `confirmarVenta(int cantidad)`: Reduce stock y reserva.
- `cancelarReserva(int cantidad)`: Libera reserva.
- `obtenerCantidadDisponible()`: Devuelve `cantidad - cantidadReservada`.
- `aplicarAjuste(int ajuste)`: Lanza excepción si resulta en stock negativo.

---

### `CarritoDeCompras.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `comprador` | `Comprador` | Comprador propietario. |
| `items` | `Map<String, Integer>` | Mapa de `productoId` → `cantidad`. |

**Métodos**:
- `agregarItem(String productId, int cantidad)`
- `eliminarItem(String productId)`
- `actualizarCantidad(String productId, int cantidad)`
- `limpiar()`

---

### `Pedido.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `comprador` | `Comprador` | Comprador que realiza el pedido. |
| `items` | `Map<String, Integer>` | Productos y cantidades. |
| `montoTotal` | `Double` | Suma de precios. |
| `estado` | `EstadoDePedido` | Estado actual. |
| `estadoPago` | `EstadoDePago` | Estado del pago. |
| `fechaCreacion` | `LocalDateTime` | Fecha de creación. |
| `fechaActualizacion` | `LocalDateTime` | Última modificación. |

**Métodos**:
- `confirmarPago()`: Valida que esté en `PENDIENTE_PAGO` y cambia a `PAGADO`.
- `despachar()`: Valida que esté `PAGADO` y cambia a `DESPACHADO`.
- `entregar()`: Valida que esté `DESPACHADO` y cambia a `ENTREGADO`.
- `finalizar()`: Valida que esté `ENTREGADO` y cambia a `FINALIZADO`.
- `estaFinalizado()`: Devuelve `true` si estado es `FINALIZADO`.

---

### `Factura.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `pedido` | `Pedido` | Pedido asociado. |
| `numeroFactura` | `String` | Número de factura. |
| `subtotal` | `Double` | Monto sin impuestos. |
| `impuestos` | `Double` | Monto de impuestos. |
| `total` | `Double` | Subtotal + impuestos. |
| `fechaEmision` | `LocalDateTime` | Fecha de emisión. |

---

### `Envio.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `pedido` | `Pedido` | Pedido a enviar. |
| `numeroSeguimiento` | `String` | Número de guía. |
| `bodegaOrigen` | `Bodega` | Bodega de salida. |
| `direccionDestino` | `String` | Dirección de entrega. |
| `estado` | `EstadoDeEnvio` | Estado del envío. |

**Métodos**:
- `iniciarEnvio(String numeroSeguimiento)`: Cambia a `EN_TRANSITO`.
- `confirmarEntrega()`: Cambia a `ENTREGADO`.

---

### `Devolucion.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `pedido` | `Pedido` | Pedido asociado. |
| `motivo` | `String` | Razón de la devolución. |
| `estado` | `EstadoDeDevolucion` | Estado de la devolución. |

**Métodos**:
- `aprobar()`: Cambia a `APROBADO`.
- `rechazar()`: Cambia a `RECHAZADO`.
- `completar()`: Cambia a `COMPLETADO`.

---

### `Reembolso.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `devolucion` | `Devolucion` | Devolución asociada. |
| `monto` | `Double` | Cantidad a reembolsar. |
| `metodoPago` | `String` | Medio de reembolso. |

---

## Diagrama de Relaciones

┌─────────────┐ ┌─────────────┐
│ Usuario │ │ Comprador │
│─────────────│ │─────────────│
│ - id │1 1 │ - id │
│ - nombre │◄─────────│ - usuario │
│ - email │ │ - dirPrin │
│ - rol │ │ - dirsAdic │
│ - estado │ │ - estadoCom │
└─────────────┘ └─────────────┘
│ │1
│1 │
│ │
│1 │
▼ ┌──────▼──────┐
┌─────────────┐ │ Carrito │
│ Vendedor │ │─────────────│
│─────────────│ │ - id │
│ - id │ │ - comprador │
│ - usuario │ │ - items │
│ - identifT │ └─────────────┘
│ - bodegas │
└─────────────┘
│1
│
│*
▼
┌─────────────┐ ┌─────────────┐
│ Bodega │ │ Producto │
│─────────────│ │─────────────│
│ - id │1 │ - id │
│ - nombre │◄─────────│ - nombre │
│ - direccion │ │ - desc │
│ - tipo │ │ - precio │
│ - vendedor │ │ - tipo │
└─────────────┘ │ - vendedor │
│1 │ - publicado│
│ └─────────────┘
│ │1
▼ │
┌─────────────┐ │
│ Inventario │ │
│─────────────│ │
│ - id │ │
│ - producto │ │
│ - bodega │ │
│ - cantidad │ │
│ - reservado │ │
└─────────────┘ │
│*
▼
┌─────────────┐
│ Pedido │
│─────────────│
│ - id │
│ - comprador │
│ - items │
│ - total │
│ - estado │
│ - estadoPago│
│ - fechas │
└─────────────┘
│1
│
┌─────────────────────┼─────────────────────┐
│ │ │
▼ ▼ ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│ Factura │ │ Envio │ │ Devolucion │
│─────────────│ │─────────────│ │─────────────│
│ - id │ │ - id │ │ - id │
│ - pedido │ │ - pedido │ │ - pedido │
│ - numFact │ │ - seguim │ │ - motivo │
│ - subtotal │ │ - bodegaOrg │ │ - estado │
│ - impuestos │ │ - dirDest │ └─────────────┘
│ - total │ │ - estado │ │1
│ - fechaEm │ └─────────────┘ │
└─────────────┘ │
│1
▼
┌─────────────┐
│ Reembolso │
│─────────────│
│ - id │
│ - devolucion│
│ - monto │
│ - metodoPago│
└─────────────┘

---

## Matriz de Responsabilidades (según especificación)

| Proceso | Comprador | Vendedor | Operador Logístico | Administrador |
|---------|-----------|----------|-------------------|---------------|
| Registro de Vendedores |  |  |  | ✓ |
| Registro de Productos |  | ✓ |  |  |
| Administración de Inventario |  | ✓ | ✓ |  |
| Gestión de Pedidos | ✓ | ✓ | ✓ |  |
| Gestión de Reembolsos | ✓ |  |  | ✓ |

---

## Validaciones Críticas Implementadas

| Validación | Implementación en el Modelo |
|------------|-----------------------------|
| **Inventario**: No se puede reservar inventario inexistente o dañado. | `Inventario.reservar()` lanza `IllegalStateException` si no hay disponible. |
| **Pedidos**: Un pedido finalizado no puede modificarse. | `Pedido.estaFinalizado()` usado en métodos de transición para prevenir cambios. |
| **Usuarios**: Documento de identidad y correo únicos. | Control en capa de aplicación / repositorio (no en el dominio). |
| **Usuarios**: Cada usuario tiene un solo rol. | `Usuario.rol` es final y se asigna al crear. |

---

## Restricciones Generales (RG)

- **RG-01**: Toda operación debe ser ejecutada por un usuario autenticado.
- **RG-02**: Cada usuario tendrá un único rol dentro del sistema.

Estas restricciones se aplican a nivel de aplicación (servicios y controladores).

---

=======
# NEXUSMARKET - MODELO DE DOMINIO

## Introducción

Este documento describe el modelo de dominio de la plataforma **NexusMarket**, basado en la Especificación Funcional del Negocio. El modelo captura las entidades principales, sus atributos, comportamientos y relaciones, así como las reglas de negocio y restricciones críticas.

El diseño sigue los principios de **Domain-Driven Design (DDD)** y se alinea con la estructura del repositorio de ejemplo proporcionado.

## Estructura de Paquetes

application/
├── domain/
│ ├── entities/
│ │ ├── Usuario.java
│ │ ├── Comprador.java
│ │ ├── Vendedor.java
│ │ ├── Bodega.java
│ │ ├── Producto.java
│ │ ├── Inventario.java
│ │ ├── CarritoDeCompras.java
│ │ ├── Pedido.java
│ │ ├── Factura.java
│ │ ├── Envio.java
│ │ ├── Devolucion.java
│ │ └── Reembolso.java
│ └── valueobjects/
│ ├── CatalogoDominio.java
│ ├── RolDeUsuario.java
│ ├── EstadoDeUsuario.java
│ ├── EstadoComercial.java
│ ├── TipoDeProducto.java
│ ├── TipoMovimientoInventario.java
│ ├── EstadoDePedido.java
│ ├── EstadoDePago.java
│ ├── EstadoDeEnvio.java
│ └── EstadoDeDevolucion.java


---

## Objetos de Valor (Value Objects)

Todos los objetos de valor extienden de la clase base `CatalogoDominio` y son inmutables.

### `CatalogoDominio.java` (Clase Base)

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `codigo` | `String` | Identificador único del catálogo. |
| `nombre` | `String` | Nombre descriptivo. |
| `descripcion` | `String` | Descripción funcional. |

**Métodos**: Getters y `equals/hashCode` basados en `codigo`.

---

### `RolDeUsuario.java`

**Valores definidos**:
- `COMPRADOR`
- `VENDEDOR`
- `OPERADOR_LOGISTICO`
- `ADMINISTRADOR`
- `SUPERVISOR`

---

### `EstadoDeUsuario.java`

**Valores definidos**:
- `ACTIVO`
- `BLOQUEADO`
- `INACTIVO`

---

### `EstadoComercial.java`

**Valores definidos**:
- `ACTIVO`
- `RESTRINGIDO`

---

### `TipoDeProducto.java`

**Valores definidos**:
- `FISICO`
- `DIGITAL`

---

### `TipoMovimientoInventario.java`

**Valores definidos**:
- `INGRESO`
- `RESERVA`
- `SALIDA_POR_VENTA`
- `AJUSTE`
- `DEVOLUCION`

---

### `EstadoDePedido.java`

**Valores definidos**:
- `CARRITO`
- `PENDIENTE_PAGO`
- `PAGADO`
- `DESPACHADO`
- `ENTREGADO`
- `FINALIZADO`

---

### `EstadoDePago.java`

**Valores definidos**:
- `PENDIENTE`
- `CONFIRMADO`
- `RECHAZADO`

---

### `EstadoDeEnvio.java`

**Valores definidos**:
- `PENDIENTE`
- `EN_TRANSITO`
- `ENTREGADO`

---

### `EstadoDeDevolucion.java`

**Valores definidos**:
- `SOLICITADO`
- `APROBADO`
- `RECHAZADO`
- `COMPLETADO`

---

## Entidades del Dominio

A continuación se describen las entidades con sus atributos principales y métodos de negocio.

---

### `Usuario.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador único. |
| `nombreCompleto` | `String` | Nombre oficial. |
| `correoElectronico` | `String` | Medio de acceso y comunicación. |
| `rol` | `RolDeUsuario` | Rol único del usuario. |
| `estado` | `EstadoDeUsuario` | Condición operativa. |

**Métodos**:
- `bloquear()`: Cambia estado a `BLOQUEADO`.
- `activar()`: Cambia estado a `ACTIVO`.
- `estaActivo()`: Devuelve `true` si estado es `ACTIVO`.

---

### `Comprador.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `usuario` | `Usuario` | Usuario asociado. |
| `direccionPrincipal` | `String` | Ubicación habitual para entregas. |
| `direccionesAdicionales` | `List<String>` | Otras ubicaciones de entrega. |
| `estadoComercial` | `EstadoComercial` | Condición para realizar compras. |

**Métodos**:
- `agregarDireccion(String direccion)`
- `restringir()`: Cambia estado a `RESTRINGIDO`.

---

### `Vendedor.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `usuario` | `Usuario` | Usuario asociado. |
| `identificacionTributaria` | `String` | NIT / RUC. |
| `bodegas` | `List<Bodega>` | Bodegas asociadas. |

**Métodos**:
- `agregarBodega(Bodega bodega)`

---

### `Bodega.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `nombre` | `String` | Nombre de la bodega. |
| `direccion` | `String` | Ubicación física. |
| `tipo` | `String` | `MARKETPLACE` o `VENDEDOR`. |
| `vendedor` | `Vendedor` | Vendedor asociado (null si es del Marketplace). |

---

### `Producto.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `nombre` | `String` | Nombre del producto. |
| `descripcion` | `String` | Descripción. |
| `precio` | `Double` | Precio unitario. |
| `tipo` | `TipoDeProducto` | `FISICO` o `DIGITAL`. |
| `vendedor` | `Vendedor` | Vendedor que lo ofrece. |
| `publicado` | `boolean` | Indica si es visible en el catálogo. |

**Métodos**:
- `publicar()`: Cambia `publicado` a `true`.
- `ocultar()`: Cambia `publicado` a `false`.

---

### `Inventario.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `producto` | `Producto` | Producto asociado. |
| `bodega` | `Bodega` | Bodega donde se almacena. |
| `cantidad` | `int` | Cantidad total en stock. |
| `cantidadReservada` | `int` | Cantidad apartada para pedidos en proceso. |

**Métodos**:
- `agregarStock(int cantidad)`
- `reservar(int cantidad)`: Lanza excepción si no hay suficiente disponible.
- `confirmarVenta(int cantidad)`: Reduce stock y reserva.
- `cancelarReserva(int cantidad)`: Libera reserva.
- `obtenerCantidadDisponible()`: Devuelve `cantidad - cantidadReservada`.
- `aplicarAjuste(int ajuste)`: Lanza excepción si resulta en stock negativo.

---

### `CarritoDeCompras.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `comprador` | `Comprador` | Comprador propietario. |
| `items` | `Map<String, Integer>` | Mapa de `productoId` → `cantidad`. |

**Métodos**:
- `agregarItem(String productId, int cantidad)`
- `eliminarItem(String productId)`
- `actualizarCantidad(String productId, int cantidad)`
- `limpiar()`

---

### `Pedido.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `comprador` | `Comprador` | Comprador que realiza el pedido. |
| `items` | `Map<String, Integer>` | Productos y cantidades. |
| `montoTotal` | `Double` | Suma de precios. |
| `estado` | `EstadoDePedido` | Estado actual. |
| `estadoPago` | `EstadoDePago` | Estado del pago. |
| `fechaCreacion` | `LocalDateTime` | Fecha de creación. |
| `fechaActualizacion` | `LocalDateTime` | Última modificación. |

**Métodos**:
- `confirmarPago()`: Valida que esté en `PENDIENTE_PAGO` y cambia a `PAGADO`.
- `despachar()`: Valida que esté `PAGADO` y cambia a `DESPACHADO`.
- `entregar()`: Valida que esté `DESPACHADO` y cambia a `ENTREGADO`.
- `finalizar()`: Valida que esté `ENTREGADO` y cambia a `FINALIZADO`.
- `estaFinalizado()`: Devuelve `true` si estado es `FINALIZADO`.

---

### `Factura.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `pedido` | `Pedido` | Pedido asociado. |
| `numeroFactura` | `String` | Número de factura. |
| `subtotal` | `Double` | Monto sin impuestos. |
| `impuestos` | `Double` | Monto de impuestos. |
| `total` | `Double` | Subtotal + impuestos. |
| `fechaEmision` | `LocalDateTime` | Fecha de emisión. |

---

### `Envio.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `pedido` | `Pedido` | Pedido a enviar. |
| `numeroSeguimiento` | `String` | Número de guía. |
| `bodegaOrigen` | `Bodega` | Bodega de salida. |
| `direccionDestino` | `String` | Dirección de entrega. |
| `estado` | `EstadoDeEnvio` | Estado del envío. |

**Métodos**:
- `iniciarEnvio(String numeroSeguimiento)`: Cambia a `EN_TRANSITO`.
- `confirmarEntrega()`: Cambia a `ENTREGADO`.

---

### `Devolucion.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `pedido` | `Pedido` | Pedido asociado. |
| `motivo` | `String` | Razón de la devolución. |
| `estado` | `EstadoDeDevolucion` | Estado de la devolución. |

**Métodos**:
- `aprobar()`: Cambia a `APROBADO`.
- `rechazar()`: Cambia a `RECHAZADO`.
- `completar()`: Cambia a `COMPLETADO`.

---

### `Reembolso.java`

| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Identificador. |
| `devolucion` | `Devolucion` | Devolución asociada. |
| `monto` | `Double` | Cantidad a reembolsar. |
| `metodoPago` | `String` | Medio de reembolso. |

---

## Diagrama de Relaciones

┌─────────────┐ ┌─────────────┐
│ Usuario │ │ Comprador │
│─────────────│ │─────────────│
│ - id │1 1 │ - id │
│ - nombre │◄─────────│ - usuario │
│ - email │ │ - dirPrin │
│ - rol │ │ - dirsAdic │
│ - estado │ │ - estadoCom │
└─────────────┘ └─────────────┘
│ │1
│1 │
│ │
│1 │
▼ ┌──────▼──────┐
┌─────────────┐ │ Carrito │
│ Vendedor │ │─────────────│
│─────────────│ │ - id │
│ - id │ │ - comprador │
│ - usuario │ │ - items │
│ - identifT │ └─────────────┘
│ - bodegas │
└─────────────┘
│1
│
│*
▼
┌─────────────┐ ┌─────────────┐
│ Bodega │ │ Producto │
│─────────────│ │─────────────│
│ - id │1 │ - id │
│ - nombre │◄─────────│ - nombre │
│ - direccion │ │ - desc │
│ - tipo │ │ - precio │
│ - vendedor │ │ - tipo │
└─────────────┘ │ - vendedor │
│1 │ - publicado│
│ └─────────────┘
│ │1
▼ │
┌─────────────┐ │
│ Inventario │ │
│─────────────│ │
│ - id │ │
│ - producto │ │
│ - bodega │ │
│ - cantidad │ │
│ - reservado │ │
└─────────────┘ │
│*
▼
┌─────────────┐
│ Pedido │
│─────────────│
│ - id │
│ - comprador │
│ - items │
│ - total │
│ - estado │
│ - estadoPago│
│ - fechas │
└─────────────┘
│1
│
┌─────────────────────┼─────────────────────┐
│ │ │
▼ ▼ ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│ Factura │ │ Envio │ │ Devolucion │
│─────────────│ │─────────────│ │─────────────│
│ - id │ │ - id │ │ - id │
│ - pedido │ │ - pedido │ │ - pedido │
│ - numFact │ │ - seguim │ │ - motivo │
│ - subtotal │ │ - bodegaOrg │ │ - estado │
│ - impuestos │ │ - dirDest │ └─────────────┘
│ - total │ │ - estado │ │1
│ - fechaEm │ └─────────────┘ │
└─────────────┘ │
│1
▼
┌─────────────┐
│ Reembolso │
│─────────────│
│ - id │
│ - devolucion│
│ - monto │
│ - metodoPago│
└─────────────┘

---

## Matriz de Responsabilidades (según especificación)

| Proceso | Comprador | Vendedor | Operador Logístico | Administrador |
|---------|-----------|----------|-------------------|---------------|
| Registro de Vendedores |  |  |  | ✓ |
| Registro de Productos |  | ✓ |  |  |
| Administración de Inventario |  | ✓ | ✓ |  |
| Gestión de Pedidos | ✓ | ✓ | ✓ |  |
| Gestión de Reembolsos | ✓ |  |  | ✓ |

---

## Validaciones Críticas Implementadas

| Validación | Implementación en el Modelo |
|------------|-----------------------------|
| **Inventario**: No se puede reservar inventario inexistente o dañado. | `Inventario.reservar()` lanza `IllegalStateException` si no hay disponible. |
| **Pedidos**: Un pedido finalizado no puede modificarse. | `Pedido.estaFinalizado()` usado en métodos de transición para prevenir cambios. |
| **Usuarios**: Documento de identidad y correo únicos. | Control en capa de aplicación / repositorio (no en el dominio). |
| **Usuarios**: Cada usuario tiene un solo rol. | `Usuario.rol` es final y se asigna al crear. |

---

## Restricciones Generales (RG)

- **RG-01**: Toda operación debe ser ejecutada por un usuario autenticado.
- **RG-02**: Cada usuario tendrá un único rol dentro del sistema.

Estas restricciones se aplican a nivel de aplicación (servicios y controladores).

---

**Fin del documento**