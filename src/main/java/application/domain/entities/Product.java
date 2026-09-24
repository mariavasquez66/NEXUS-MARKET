package application.domain.entities;

import application.domain.valueobjects.ProductStatus;
import application.domain.valueobjects.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductType type;
    private Seller seller;
    private ProductStatus status;
    private List<String> variants;

    public Product(String id, String name, String description, BigDecimal price, ProductType type, Seller seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.seller = seller;
        this.status = ProductStatus.SUSPENDED;
        this.variants = new ArrayList<>();
    }

    public Product(String id, String name, String description, BigDecimal price, ProductType type, Seller seller, List<String> variants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.seller = seller;
        this.status = ProductStatus.SUSPENDED;
        this.variants = variants != null ? new ArrayList<>(variants) : new ArrayList<>();
    }

    public void publish() {
        this.status = ProductStatus.PUBLISHED;
    }

    public void suspend() {
        this.status = ProductStatus.SUSPENDED;
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    public boolean isPublished() {
        return ProductStatus.PUBLISHED.equals(this.status);
    }

    public boolean isSuspended() {
        return ProductStatus.SUSPENDED.equals(this.status);
    }

    public boolean isDiscontinued() {
        return ProductStatus.DISCONTINUED.equals(this.status);
    }

    public void addVariant(String variant) {
        if (this.variants == null) {
            this.variants = new ArrayList<>();
        }
        if (variant != null && !variant.isBlank() && !this.variants.contains(variant)) {
            this.variants.add(variant);
        }
    }
}