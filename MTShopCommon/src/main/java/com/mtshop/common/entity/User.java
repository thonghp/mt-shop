package com.mtshop.common.entity;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "first_name", length = 100, nullable = false) // nvarchar 100, not null
    @Nationalized
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    @Nationalized
    private String lastName;

    @Column(length = 64)
    private String photos;

    private boolean enabled;

    /*
     * the default is usually Lazy for many-to-many and one-to-many relationships and Eager for one-to-one and many-to-one.
     *
     * FetchType.Eager -> when you find, select User object from database, all related Role objects will be retrieved
     * and saved into roles when the transaction ends, it is still stored in roles
     * FetchType.Lazy -> when you find, select User object from the database, it will not get related Role objects but
     * still have Role data inside roles but when the transaction ends, roles will no longer have data of Role
     *
     * Lazy
     * Advantage: save time and memory when selecting
     * Cons: causes LazyInitializationException error, when you want to get related objects, you have to open the
     * transaction again to query
     *
     * Eager
     * Advantages: can always get related objects, simple and convenient handling
     * Cons: takes a lot of time and memory when selecting, the data retrieved is redundant and unnecessary.
     */

    //    @ManyToMany(fetch = FetchType.EAGER)
    @ManyToMany()
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' + ", roles=" + roles + '}';
    }

    @Transient
    public String getPhotosImagePath() {
        if (id == null || photos == null) return "/images/default-user.png";

        return "/user-photos/" + this.id + "/" + this.photos;
    }
}
