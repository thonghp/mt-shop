package com.mtshop.common.entity;

import javax.persistence.*;

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
