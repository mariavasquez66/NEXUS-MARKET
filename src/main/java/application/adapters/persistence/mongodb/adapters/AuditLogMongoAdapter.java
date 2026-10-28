package application.adapters.persistence.mongodb.adapters;

import application.adapters.persistence.mongodb.mappers.AuditLogMongoMapper;
import application.adapters.persistence.mongodb.repositories.AuditLogMongoRepository;
import application.domain.entities.AuditLog;
import application.domain.ports.out.AuditRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditLogMongoAdapter implements AuditRepositoryPort {

    private final AuditLogMongoRepository repository;

    public AuditLogMongoAdapter(AuditLogMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        var doc = AuditLogMongoMapper.toDocument(auditLog);
        var savedDoc = repository.save(doc);
        return AuditLogMongoMapper.toDomain(savedDoc);
    }

    @Override
    public List<AuditLog> findByUserId(String userId) {
        return repository.findByUserId(userId).stream()
                .map(AuditLogMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByEntityNameAndEntityId(String entityName, String entityId) {
        return repository.findByEntityNameAndEntityId(entityName, entityId).stream()
                .map(AuditLogMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findAll() {
        return repository.findAll().stream()
                .map(AuditLogMongoMapper::toDomain)
                .collect(Collectors.toList());
    }
}

