package com.mtshop.common.entity;

import javax.persistence.*;
import java.util.Objects;

/* map to table */
@Entity
@Table(name = "role")
/* map to table */
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false, unique = true) // varchar 40, not null
    private String name;

    @Column(length = 150, nullable = false)
    private String description;


    public Role() {
    }

    // find by id
    public Role(Integer id) {
        this.id = id;
    }

    public Role(String name) {
        this.name = name;
    }

    // insert
    public Role(String name, String description) {
        this.description = description;
        this.name = name;
    }

    // update
    public Role(Integer id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
