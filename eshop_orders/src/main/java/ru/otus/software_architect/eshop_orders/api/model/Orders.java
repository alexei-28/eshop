package ru.otus.software_architect.eshop_orders.api.model;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String username;
    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date created;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date updated;
    private String paymentCardNumber;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;
    private String promoCode;
    @NotNull
    private double totalAmount;
    @NotNull
    private String currenty;
    @NotNull
    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductEntry> productEntities = new HashSet<>();

    public void addProduct(Product product, int quantity) {
        productEntities.add(new ProductEntry(product, quantity, this));
    }

    public void updateProductQuantity(int productId, int quantity) {
        ProductEntry productEntry = getProductEntity(productId);
        productEntry.setQuantity(quantity);
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

    public void removeProduct(int productId) {
        ProductEntry productEntry = getProductEntity(productId);
        productEntities.remove(productEntry);
    }

    public Set<ProductEntry> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(Set<ProductEntry> productEntities) {
        this.productEntities = productEntities;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaymentCardNumber() {
        return paymentCardNumber;
    }

    public void setPaymentCardNumber(String paymentCardNumber) {
        this.paymentCardNumber = paymentCardNumber;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrenty() {
        return currenty;
    }

    public void setCurrenty(String currenty) {
        this.currenty = currenty;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", username=" + username +
                ", created=" + created +
                ", updated=" + updated +
                ", paymentCardNumber='" + paymentCardNumber + '\'' +
                ", shipping=" + shipping +
                ", promoCode='" + promoCode + '\'' +
                ", totalAmount=" + totalAmount +
                ", currenty='" + currenty + '\'' +
                ", productEntities=" + productEntities +
                '}';
    }
}

