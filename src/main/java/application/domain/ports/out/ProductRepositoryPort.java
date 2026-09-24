package application.domain.ports.out;

import application.domain.entities.Product;
import application.domain.valueobjects.ProductStatus;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    List<Product> findByStatus(ProductStatus status);
    List<Product> findBySellerId(String sellerId);
    boolean existsById(String id);
    void deleteById(String id);
}
