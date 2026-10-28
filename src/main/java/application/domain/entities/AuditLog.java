package application.domain.entities;

import java.time.LocalDateTime;

public class AuditLog {
    private String id;
    private String action;
    private String userId;
    private String userRole;
    private String entityName;
    private String entityId;
    private String details;
    private LocalDateTime timestamp;

    public AuditLog(String id, String action, String userId, String userRole, String entityName, String entityId, String details, LocalDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.userId = userId;
        this.userRole = userRole;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

