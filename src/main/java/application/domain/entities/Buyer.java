package application.domain.entities;

import application.domain.valueobjects.CommercialStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Buyer {
    private String id;
    private User user;
    private String mainAddress;
    private List<String> additionalAddresses;
    private CommercialStatus commercialStatus;

    public Buyer(User user, String mainAddress) {
        this.user = user;
        this.mainAddress = mainAddress;
        this.additionalAddresses = new ArrayList<>();
        this.commercialStatus = CommercialStatus.ACTIVE;
    }

    public Buyer(String id, User user, String mainAddress) {
        this.id = id;
        this.user = user;
        this.mainAddress = mainAddress;
        this.additionalAddresses = new ArrayList<>();
        this.commercialStatus = CommercialStatus.ACTIVE;
    }

    public void addAddress(String address) {
        if (this.additionalAddresses == null) {
            this.additionalAddresses = new ArrayList<>();
        }
        if (address != null && !address.isBlank() && !this.additionalAddresses.contains(address)) {
            this.additionalAddresses.add(address);
        }
    }

    public void updateMainAddress(String address) {
        this.mainAddress = address;
    }

    public void restrict() {
        this.commercialStatus = CommercialStatus.RESTRICTED;
    }

    public void activate() {
        this.commercialStatus = CommercialStatus.ACTIVE;
    }

    public boolean isActive() {
        return CommercialStatus.ACTIVE.equals(this.commercialStatus);
    }

    public boolean isRestricted() {
        return CommercialStatus.RESTRICTED.equals(this.commercialStatus);
    }
}
