package application.domain.services.user;

import application.domain.entities.User;
import application.domain.exceptions.common.InvalidDataException;
import application.domain.exceptions.user.DuplicateEmailException;
import application.domain.exceptions.user.DuplicateIdentificationException;
import application.domain.exceptions.user.InvalidRoleException;
import application.domain.exceptions.user.UserNotFoundException;
import application.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryPort userRepositoryPort;

    public User createUser(User user) {
        if (user == null) {
            throw new InvalidDataException("User payload cannot be null.");
        }
        if (user.getId() == null || user.getId().isBlank()) {
            throw new InvalidDataException("User ID is mandatory and cannot be blank.");
        }
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new InvalidDataException("Full name is mandatory and cannot be blank.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new InvalidDataException("A valid email address is mandatory.");
        }
        if (user.getRole() == null) {
            throw new InvalidRoleException("A user must have a valid role assigned (RG-02).");
        }

        if (userRepositoryPort.existsById(user.getId())) {
            throw new DuplicateIdentificationException(user.getId());
        }
        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }

        return userRepositoryPort.save(user);
    }

    public User getUserById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("User ID cannot be null or blank.");
        }
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepositoryPort.findAll();
    }

    public User updateUser(String id, String fullName, String email) {
        User user = getUserById(id);

        if (fullName == null || fullName.isBlank()) {
            throw new InvalidDataException("Full name cannot be blank.");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new InvalidDataException("A valid email address is mandatory.");
        }

        if (!user.getEmail().equalsIgnoreCase(email)) {
            userRepositoryPort.findByEmail(email).ifPresent(existing -> {
                if (!existing.getId().equals(user.getId())) {
                    throw new DuplicateEmailException(email);
                }
            });
            user.setEmail(email);
        }

        user.setFullName(fullName);
        return userRepositoryPort.save(user);
    }

    public User blockUser(String id) {
        User user = getUserById(id);
        user.block();
        return userRepositoryPort.save(user);
    }

    public User activateUser(String id) {
        User user = getUserById(id);
        user.activate();
        return userRepositoryPort.save(user);
    }

    public void deleteUser(String id) {
        User user = getUserById(id);
        user.delete();
        userRepositoryPort.save(user);
    }
}
