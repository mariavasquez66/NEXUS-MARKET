package application.domain.ports.out;

import application.domain.entities.Refund;
import java.util.List;
import java.util.Optional;

public interface RefundRepositoryPort {
    Refund save(Refund refund);
    Optional<Refund> findById(String id);
    Optional<Refund> findByReturnRequestId(String returnRequestId);
    List<Refund> findAll();
}

