package com.mtshop.common.entity;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class AbstractAddress extends IdBasedEntity {

    @Column(name = "first_name", nullable = false, length = 45)
    @Nationalized
    protected String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    @Nationalized
    protected String lastName;

    @Column(name = "phone_number", length = 15)
    protected String phoneNumber;

    @Column(name = "address_line_1", length = 64)
    @Nationalized
    protected String addressLine1;

    @Column(name = "address_line_2", length = 64)
    @Nationalized
    protected String addressLine2;

    @Column(length = 45)
    @Nationalized
    protected String city;

    @Column(length = 45)
    @Nationalized
    protected String state;

    @Column(name = "postal_code", length = 10)
    protected String postalCode;
}