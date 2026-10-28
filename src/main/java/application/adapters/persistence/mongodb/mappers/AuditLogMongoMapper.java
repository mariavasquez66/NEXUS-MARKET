package application.adapters.persistence.mongodb.mappers;

import application.adapters.persistence.mongodb.documents.AuditLogDocument;
import application.domain.entities.AuditLog;

public final class AuditLogMongoMapper {

    private AuditLogMongoMapper() {
    }

    public static AuditLogDocument toDocument(AuditLog domain) {
        if (domain == null) {
            return null;
        }
        return new AuditLogDocument(
                domain.getId(),
                domain.getAction(),
                domain.getUserId(),
                domain.getUserRole(),
                domain.getEntityName(),
                domain.getEntityId(),
                domain.getDetails(),
                domain.getTimestamp()
        );
    }

    public static AuditLog toDomain(AuditLogDocument doc) {
        if (doc == null) {
            return null;
        }
        return new AuditLog(
                doc.getId(),
                doc.getAction(),
                doc.getUserId(),
                doc.getUserRole(),
                doc.getEntityName(),
                doc.getEntityId(),
                doc.getDetails(),
                doc.getTimestamp()
        );
    }
}

