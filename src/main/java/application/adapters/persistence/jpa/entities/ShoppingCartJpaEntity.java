package application.adapters.persistence.jpa.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCartJpaEntity {

    @Id
    @Column(length = 64)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false, unique = true)
    private BuyerJpaEntity buyer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItemJpaEntity> items = new ArrayList<>();

    public ShoppingCartJpaEntity() {
    }

    public ShoppingCartJpaEntity(String id, BuyerJpaEntity buyer, List<CartItemJpaEntity> items) {
        this.id = id;
        this.buyer = buyer;
        this.items = items != null ? items : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BuyerJpaEntity getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerJpaEntity buyer) {
        this.buyer = buyer;
    }

    public List<CartItemJpaEntity> getItems() {
        return items;
    }

    public void setItems(List<CartItemJpaEntity> items) {
        this.items = items;
    }
}

