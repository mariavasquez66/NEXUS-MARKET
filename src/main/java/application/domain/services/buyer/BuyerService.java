package application.domain.services.buyer;

import application.domain.entities.Buyer;
import application.domain.entities.User;
import application.domain.exceptions.buyer.BuyerNotFoundException;
import application.domain.exceptions.buyer.InvalidAddressException;
import application.domain.exceptions.common.DuplicateEntityException;
import application.domain.exceptions.common.InvalidDataException;
import application.domain.exceptions.user.InactiveUserException;
import application.domain.exceptions.user.InvalidRoleException;
import application.domain.exceptions.user.UserNotFoundException;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueobjects.UserRole;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public Buyer registerBuyer(String id, String userId, String mainAddress) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Buyer ID cannot be null or blank.");
        }
        if (userId == null || userId.isBlank()) {
            throw new InvalidDataException("User ID cannot be null or blank.");
        }
        if (mainAddress == null || mainAddress.isBlank()) {
            throw new InvalidAddressException("Main address cannot be null or blank.");
        }

        if (buyerRepositoryPort.existsById(id)) {
            throw new DuplicateEntityException("A Buyer with identifier " + id + " already exists.");
        }
        if (buyerRepositoryPort.existsByUserId(userId)) {
            throw new DuplicateEntityException("A Buyer profile is already associated with user ID " + userId);
        }

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isActive()) {
            throw new InactiveUserException("Associated user is inactive or blocked: " + userId);
        }

        if (!UserRole.BUYER.equals(user.getRole())) {
            throw new InvalidRoleException("User must have BUYER role to register as a Buyer. Actual role: "
                    + (user.getRole() != null ? user.getRole().getCode() : "null"));
        }

        Buyer buyer = new Buyer(id, user, mainAddress);
        return buyerRepositoryPort.save(buyer);
    }

    public Buyer getBuyerById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("Buyer ID cannot be null or blank.");
        }
        return buyerRepositoryPort.findById(id)
                .orElseThrow(() -> new BuyerNotFoundException(id));
    }

    public List<Buyer> getAllBuyers() {
        return buyerRepositoryPort.findAll();
    }

    public Buyer addAdditionalAddress(String buyerId, String address) {
        if (address == null || address.isBlank()) {
            throw new InvalidAddressException("Additional address cannot be null or blank.");
        }
        Buyer buyer = getBuyerById(buyerId);
        buyer.addAddress(address);
        return buyerRepositoryPort.save(buyer);
    }

    public Buyer updateMainAddress(String buyerId, String address) {
        if (address == null || address.isBlank()) {
            throw new InvalidAddressException("Main address cannot be null or blank.");
        }
        Buyer buyer = getBuyerById(buyerId);
        buyer.updateMainAddress(address);
        return buyerRepositoryPort.save(buyer);
    }

    public Buyer restrictBuyer(String buyerId) {
        Buyer buyer = getBuyerById(buyerId);
        buyer.restrict();
        return buyerRepositoryPort.save(buyer);
    }

    public Buyer activateBuyer(String buyerId) {
        Buyer buyer = getBuyerById(buyerId);
        buyer.activate();
        return buyerRepositoryPort.save(buyer);
    }
}
