package com.mtshop.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
@Getter
@Setter
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

    @Override
    public String toString() {
        return "Customer [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

    @Transient
    public String getAddress() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) address += " " + lastName;

        if (!addressLine1.isEmpty()) address += ", " + addressLine1;

        if (addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;

        if (!city.isEmpty()) address += ", " + city;

        if (state != null && !state.isEmpty()) address += ", " + state;

        address += ", " + country.getName();

        if (!postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
        if (!phoneNumber.isEmpty()) address += ". Số điện thoại: " + phoneNumber;

        return address;
    }
}
