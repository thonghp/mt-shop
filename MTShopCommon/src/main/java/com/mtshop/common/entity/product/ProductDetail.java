package com.mtshop.common.entity.product;

import com.mtshop.common.entity.IdBasedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_details")
@Getter
@Setter
public class ProductDetail extends IdBasedEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductDetail() {
    }

    public ProductDetail(String name, String value, Product product) {
        this.name = name;
        this.value = value;
        this.product = product;
    }

    public ProductDetail(Integer id, String name, String value, Product product) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductDetail{" + "id=" + id + ", name='" + name + '\'' + ", value='" + value + '\'' + ", product=" + product + '}';
    }
}
