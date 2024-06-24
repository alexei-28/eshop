package ru.otus.software_architect.eshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import ru.otus.software_architect.eshop.annotation.Exclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class ProductEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Exclude
    private int id;
    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Exclude
    private Date created;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Exclude
    private Date updated;
    private int quantity;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Product product;
    @Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;
    @Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Cart cart;

    public ProductEntry() {

    }

    public ProductEntry(Product product, int quantity, Cart cart) {
        this.created = new Date();
        this.product = product;
        this.quantity = quantity;
        this.cart = cart;
    }

    public ProductEntry(Product product, int quantity, Orders orders) {
        this.created = new Date();
        this.product = product;
        this.quantity = quantity;
        this.orders = orders;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "\nProductEntity {" +
                "id = " + id +
                ", created = " + created +
                ", updated = " + updated +
                ", quantity = " + quantity +
                ", product = " + product +
                '}';
    }
}
