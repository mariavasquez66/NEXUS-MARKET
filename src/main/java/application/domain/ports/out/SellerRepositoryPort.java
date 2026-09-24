package application.domain.ports.out;

import application.domain.entities.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerRepositoryPort {
    Seller save(Seller seller);
    Optional<Seller> findById(String id);
    Optional<Seller> findByUserId(String userId);
    Optional<Seller> findByTaxId(String taxId);
    boolean existsById(String id);
    boolean existsByUserId(String userId);
    boolean existsByTaxId(String taxId);
    List<Seller> findAll();
}
