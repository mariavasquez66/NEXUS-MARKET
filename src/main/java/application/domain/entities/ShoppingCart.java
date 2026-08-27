package application.domain.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ShoppingCart {
    private String id;
    private Buyer buyer;
    private Map<String, Integer> items;

    public ShoppingCart(Buyer buyer) {
        this.buyer = buyer;
        this.items = new HashMap<>();
    }

    public void addItem(String productId, int quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void updateQuantity(String productId, int quantity) {
        if (quantity <= 0) {
            items.remove(productId);
        } else {
            items.put(productId, quantity);
        }
    }

    public void clear() {
        items.clear();
    }
}