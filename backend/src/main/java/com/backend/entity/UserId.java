package com.backend.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {
    private Integer idUser;
    private Integer adminId;

    // Constructors, getters, setters, equals() and hashCode() methods

    public UserId() {}

    public UserId(Integer idUser, Integer adminId) {
        this.idUser = idUser;
        this.adminId = adminId;
    }

    // Override equals() and hashCode() methods based on the composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return idUser.equals(userId.idUser) && adminId.equals(userId.adminId);
    }

    @Override
    public int hashCode() {
        return 31 * idUser.hashCode() + adminId.hashCode();
    }
}
