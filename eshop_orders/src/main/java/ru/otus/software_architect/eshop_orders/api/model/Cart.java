package ru.otus.software_architect.eshop_orders.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;
import java.util.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private int id;
    @NotNull
    private String cartId;
    @NotNull
    private String username;
    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date created;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date updated;
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<ProductEntry> productEntities = new HashSet<>();

    public void addProduct(Product product, int quantity) {
        productEntities.add(new ProductEntry(product, quantity, this));
    }

    public double getTotalAmount() {
        return productEntities.stream()
                .mapToDouble(productEntity -> productEntity.getProduct().getPrice())
                .sum();
    }

    public String getCurrency() {
        return productEntities.stream()
                .findFirst()
                .map(productEntity -> productEntity.getProduct().getCurrency())
                .orElse(null);
    }

    public void updateProductQuantity(int productId, int quantity) {
        ProductEntry productEntry = getProductEntity(productId);
        productEntry.setQuantity(quantity);
    }

    public void removeProduct(int productId) {
        ProductEntry productEntry = getProductEntity(productId);
        productEntities.remove(productEntry);
    }

    public ProductEntry getProductEntity(int productId) {
        Optional<ProductEntry> findProductEntityOptional = productEntities
                .stream()
                .filter(productEntity -> productEntity.getProduct().getId() == productId)
                .findFirst();
        if (findProductEntityOptional.isPresent()) {
            return findProductEntityOptional.get();
        } else {
            throw new InvalidParameterException("Not found product with id = " + productId);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<ProductEntry> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(Set<ProductEntry> productEntities) {
        this.productEntities = productEntities;
    }

    @Override
    public String toString() {
        return "Cart {" +
                "id = " + id +
                ", cartId = " + cartId +
                ", username = " + username +
                ", productEntities(" + productEntities.size() + ")\n" + productEntities +
                ", created = " + created +
                ", updated = " + updated +
                '}';
    }
}
