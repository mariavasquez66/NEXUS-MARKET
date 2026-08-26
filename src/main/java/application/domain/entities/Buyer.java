package application.domain.entities;

import lombok.Getter;
import lombok.Setter;

import application.domain.valueobjects.CommercialStatus;
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

    public void addAddress(String address) {
        this.additionalAddresses.add(address);
    }

    public void restrict() {
        this.commercialStatus = CommercialStatus.RESTRICTED;
    }
    
}
