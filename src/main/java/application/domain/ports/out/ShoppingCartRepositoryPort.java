package application.domain.ports.out;

import application.domain.entities.ShoppingCart;
import java.util.Optional;

public interface ShoppingCartRepositoryPort {
    ShoppingCart save(ShoppingCart cart);
    Optional<ShoppingCart> findById(String id);
    Optional<ShoppingCart> findByBuyerId(String buyerId);
    void deleteById(String id);
}

