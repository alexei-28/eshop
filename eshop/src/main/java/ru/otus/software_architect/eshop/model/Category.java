package ru.otus.software_architect.eshop.model;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date created;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date updated;
    @OneToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "category")
    private Orders orders;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    // The "equals" compares state (the object's data)
    @Override
    public boolean equals(Object aThat) {
        // check for self-comparison
        // The "==" operator always compares identity (the object's location in
        // memory).The default implementation of equals compares identity as
        // well.
        if (this == aThat) {
            return true;
        }

        // use instanceof instead of getClass here for two reasons
        // 1. if need be, it can match any supertype, and not just one class;
        // 2. it renders an explict check for "that == null" redundant, since
        // it does the check for null already - "null instanceof [type]" always
        // returns false. (See Effective Java by Joshua Bloch.)
        if (!(aThat instanceof Category)) {
            return false;
        }
        // Alternative to the above line :
        // if ( aThat == null || aThat.getClass() != this.getClass() ) return
        // false;

        // cast to native object is now safe
        Category that = (Category) aThat;
        // now a proper field-by-field evaluation can be made
        // segment BS (sequence number always 1) content fields reference
        // (TS,IDN,AN,PT,MN) that unique identify M2 document.
        // May compare only by this fields
        return this.getId() == (that.getId());
    }


    @Override
    public String toString() {
        return "\nCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", description='" + description + '\'' +
                '}';
    }
}
