package com.tomorrow.queueSystem.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "roles")
@XmlAccessorType(XmlAccessType.FIELD)
public class Role {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long roleId;

    @NotEmpty
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
