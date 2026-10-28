package application.adapters.persistence.jpa.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buyers")
public class BuyerJpaEntity {

    @Id
    @Column(length = 64)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserJpaEntity user;

    @Column(nullable = false, length = 255)
    private String mainAddress;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "buyer_additional_addresses", joinColumns = @JoinColumn(name = "buyer_id"))
    @Column(name = "address", length = 255)
    private List<String> additionalAddresses = new ArrayList<>();

    @Column(nullable = false, length = 50)
    private String commercialStatus;

    public BuyerJpaEntity() {
    }

    public BuyerJpaEntity(String id, UserJpaEntity user, String mainAddress, List<String> additionalAddresses, String commercialStatus) {
        this.id = id;
        this.user = user;
        this.mainAddress = mainAddress;
        this.additionalAddresses = additionalAddresses != null ? additionalAddresses : new ArrayList<>();
        this.commercialStatus = commercialStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public List<String> getAdditionalAddresses() {
        return additionalAddresses;
    }

    public void setAdditionalAddresses(List<String> additionalAddresses) {
        this.additionalAddresses = additionalAddresses;
    }

    public String getCommercialStatus() {
        return commercialStatus;
    }

    public void setCommercialStatus(String commercialStatus) {
        this.commercialStatus = commercialStatus;
    }
}

