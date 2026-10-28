package application.domain.ports.in;

import application.domain.entities.Product;
import java.util.List;
import java.util.Optional;

public interface PublicAccessPort {
    List<Product> searchPublishedProducts();
    Optional<Product> getProductDetails(String productId);
}
