package application.domain.ports.out;

import application.domain.entities.Return;
import java.util.List;
import java.util.Optional;

public interface ReturnRepositoryPort {
    Return save(Return returnRequest);
    Optional<Return> findById(String id);
    List<Return> findByOrderId(String orderId);
    List<Return> findAll();
}

