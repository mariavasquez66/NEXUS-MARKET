package application.domain.ports.in;

import application.domain.entities.Order;
import application.domain.entities.Return;
import application.domain.entities.ShoppingCart;
import application.domain.entities.User;

import java.math.BigDecimal;
import java.util.List;

public interface NaturalCustomerPort {
    ShoppingCart getCart(User buyerUser);
    ShoppingCart addItemToCart(User buyerUser, String productId, int quantity, BigDecimal unitPrice);
    ShoppingCart removeItemFromCart(User buyerUser, String productId);
    void clearCart(User buyerUser);

    Order checkoutCart(User buyerUser);
    Order payOrder(User buyerUser, String orderId);
    Order cancelOrder(User buyerUser, String orderId);
    List<Order> getMyOrders(User buyerUser);

    Return requestReturn(User buyerUser, String orderId, String reason);
}
