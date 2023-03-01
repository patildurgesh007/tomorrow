package com.tomorrow.queueSystem.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String name;

    @NotEmpty(message = "password is empty")
    private String password;

    @NotEmpty(message = "Email is Empty.")
    @Email(message = "Email is not valid.")
    private String emailAddress;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "roleId")})
    private Set<Role> roles;

    public User() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", role=" + roles +
                '}';
    }
}
