package com.mtshop.common.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
@Data
public class Customer extends IdBasedEntity {

    @Column(unique = true, length = 45, nullable = false)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "address_line_1", length = 64)
    private String addressLine1;

    @Column(name = "address_line_2", length = 64)
    private String addressLine2;

    @Column(length = 45)
    private String city;

    @Column(length = 45)
    private String state;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(length = 64, name = "verification_code")
    private String verificationCode;

    private boolean enabled;

    @Column(name = "created_time")
    private Date createdTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    public Customer() {
    }

    public Customer(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
