package com.ecom.art_courses.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Categorie.
 */
@Table("categorie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("type_categorie")
    private String typeCategorie;

    @Column("created_at")
    private Instant createdAt;

    @Column("update_at")
    private Instant updateAt;

    @Transient
    @JsonIgnoreProperties(value = { "produits", "categorie" }, allowSetters = true)
    private Set<SousCategorie> sousCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Categorie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCategorie() {
        return this.typeCategorie;
    }

    public Categorie typeCategorie(String typeCategorie) {
        this.setTypeCategorie(typeCategorie);
        return this;
    }

    public void setTypeCategorie(String typeCategorie) {
        this.typeCategorie = typeCategorie;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Categorie createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public Categorie updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Set<SousCategorie> getSousCategories() {
        return this.sousCategories;
    }

    public void setSousCategories(Set<SousCategorie> sousCategories) {
        if (this.sousCategories != null) {
            this.sousCategories.forEach(i -> i.setCategorie(null));
        }
        if (sousCategories != null) {
            sousCategories.forEach(i -> i.setCategorie(this));
        }
        this.sousCategories = sousCategories;
    }

    public Categorie sousCategories(Set<SousCategorie> sousCategories) {
        this.setSousCategories(sousCategories);
        return this;
    }

    public Categorie addSousCategorie(SousCategorie sousCategorie) {
        this.sousCategories.add(sousCategorie);
        sousCategorie.setCategorie(this);
        return this;
    }

    public Categorie removeSousCategorie(SousCategorie sousCategorie) {
        this.sousCategories.remove(sousCategorie);
        sousCategorie.setCategorie(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categorie)) {
            return false;
        }
        return id != null && id.equals(((Categorie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categorie{" +
            "id=" + getId() +
            ", typeCategorie='" + getTypeCategorie() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
