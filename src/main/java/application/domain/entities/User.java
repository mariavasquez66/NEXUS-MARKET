package application.domain.entities;

import application.domain.valueobjects.UserRole;
import application.domain.valueobjects.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String id;
    private String fullName;
    private String email;
    private UserRole role;
    private UserStatus status;
    private boolean deleted;

    public User(String id, String fullName, String email, UserRole role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.status = UserStatus.ACTIVE;
        this.deleted = false;
    }

    public void block() {
        this.status = UserStatus.BLOCKED;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void delete() {
        this.deleted = true;
        this.status = UserStatus.INACTIVE;
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status) && !deleted;
    }

    public boolean isBlocked() {
        return UserStatus.BLOCKED.equals(this.status);
    }

    public boolean isInactive() {
        return UserStatus.INACTIVE.equals(this.status);
    }
}