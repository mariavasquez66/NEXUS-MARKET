package application.domain.services.product;

import application.domain.entities.Product;
import application.domain.entities.Seller;
import application.domain.exceptions.common.DuplicateEntityException;
import application.domain.exceptions.common.InvalidDataException;
import application.domain.exceptions.product.InvalidPriceException;
import application.domain.exceptions.product.InvalidProductTypeException;
import application.domain.exceptions.product.ProductAlreadyPublishedException;
import application.domain.exceptions.product.ProductNotFoundException;
import application.domain.exceptions.seller.SellerNotFoundException;
import application.domain.exceptions.seller.SellerWithoutWarehouseException;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.valueobjects.ProductStatus;
import application.domain.valueobjects.ProductType;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class CatalogService {

    private final ProductRepositoryPort productRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;

    public Product registerProduct(String id, String name, String description, BigDecimal price,
                                   ProductType type, String sellerId, List<String> variants) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Product ID cannot be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Product name cannot be null or blank.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidDataException("Product description cannot be null or blank.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException(price != null ? price : BigDecimal.ZERO);
        }
        if (type == null) {
            throw new InvalidProductTypeException("Product type cannot be null.");
        }
        if (sellerId == null || sellerId.isBlank()) {
            throw new InvalidDataException("Seller ID cannot be null or blank.");
        }

        if (productRepositoryPort.existsById(id)) {
            throw new DuplicateEntityException("Product with ID " + id + " already exists.");
        }

        Seller seller = sellerRepositoryPort.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));

        if (ProductType.PHYSICAL.equals(type) && (seller.getWarehouses() == null || seller.getWarehouses().isEmpty())) {
            throw new SellerWithoutWarehouseException(sellerId);
        }

        Product product = new Product(id, name, description, price, type, seller, variants);
        return productRepositoryPort.save(product);
    }

    public Product getProductById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Product ID cannot be null or blank.");
        }
        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> listPublishedProducts() {
        return productRepositoryPort.findByStatus(ProductStatus.PUBLISHED);
    }

    public List<Product> listProductsBySeller(String sellerId) {
        if (sellerId == null || sellerId.isBlank()) {
            throw new InvalidDataException("Seller ID cannot be null or blank.");
        }
        if (!sellerRepositoryPort.existsById(sellerId)) {
            throw new SellerNotFoundException(sellerId);
        }
        return productRepositoryPort.findBySellerId(sellerId);
    }

    public Product updateProduct(String id, String name, String description, BigDecimal price, List<String> variants) {
        Product product = getProductById(id);

        if (name != null && !name.isBlank()) {
            product.setName(name);
        }
        if (description != null && !description.isBlank()) {
            product.setDescription(description);
        }
        if (price != null) {
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidPriceException(price);
            }
            product.setPrice(price);
        }
        if (variants != null) {
            product.setVariants(variants);
        }

        return productRepositoryPort.save(product);
    }

    public Product publishProduct(String id) {
        Product product = getProductById(id);
        if (product.isPublished()) {
            throw new ProductAlreadyPublishedException(id);
        }

        if (ProductType.PHYSICAL.equals(product.getType())) {
            Seller seller = product.getSeller();
            if (seller.getWarehouses() == null || seller.getWarehouses().isEmpty()) {
                throw new SellerWithoutWarehouseException(seller.getId());
            }
        }

        product.publish();
        return productRepositoryPort.save(product);
    }

    public Product hideProduct(String id) {
        Product product = getProductById(id);
        product.suspend();
        return productRepositoryPort.save(product);
    }

    public Product discontinueProduct(String id) {
        Product product = getProductById(id);
        product.discontinue();
        return productRepositoryPort.save(product);
    }

    public void deleteProduct(String id) {
        Product product = getProductById(id);
        product.discontinue();
        productRepositoryPort.save(product);
    }
}
