package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new ProductPrice(price);
    }

    public BigDecimal getTotalPrice(long quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return price;
    }
}