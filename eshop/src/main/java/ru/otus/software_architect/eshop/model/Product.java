package ru.otus.software_architect.eshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import ru.otus.software_architect.eshop.annotation.Exclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Exclude
    private int id;
    @NotNull
    private String productId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date created;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date updated;
    @NotNull
    private double price;
    @NotNull
    private String currency;
    @ElementCollection
    private Set<String> images;
    @Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER)
    private ProductEntry productEntry;

    public ProductEntry getProductEntry() {
        return productEntry;
    }

    public void setProductEntry(ProductEntry productEntry) {
        this.productEntry = productEntry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product {" +
                "id = " + id +
                ", productId = " + productId +
                ", name = '" + name + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", images=" + images +
                '}';
    }
}
