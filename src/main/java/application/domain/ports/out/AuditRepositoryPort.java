package application.domain.ports.out;

import application.domain.entities.AuditLog;
import java.util.List;

public interface AuditRepositoryPort {
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findByUserId(String userId);
    List<AuditLog> findByEntityNameAndEntityId(String entityName, String entityId);
    List<AuditLog> findAll();
}

