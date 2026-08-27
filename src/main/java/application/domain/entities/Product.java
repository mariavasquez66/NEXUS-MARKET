package application.domain.entities;

import application.domain.valueobjects.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private String description;
    private Double price;
    private ProductType type;
    private Seller seller;
    private boolean published;

    public Product(String id, String name, String description, Double price, ProductType type, Seller seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.seller = seller;
        this.published = false;
    }

    public void publish() {
        this.published = true;
    }

    public void unpublish() {
        this.published = false;
    }
}