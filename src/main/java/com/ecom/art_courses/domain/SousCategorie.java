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
 * A SousCategorie.
 */
@Table("sous_categorie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SousCategorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("type_sous_categorie")
    private String typeSousCategorie;

    @Column("created_at")
    private Instant createdAt;

    @Column("update_at")
    private Instant updateAt;

    @Transient
    @JsonIgnoreProperties(value = { "souscategorie", "ligneCommandes", "commandes" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "sousCategories" }, allowSetters = true)
    private Categorie categorie;

    @Column("categorie_id")
    private Long categorieId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousCategorie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeSousCategorie() {
        return this.typeSousCategorie;
    }

    public SousCategorie typeSousCategorie(String typeSousCategorie) {
        this.setTypeSousCategorie(typeSousCategorie);
        return this;
    }

    public void setTypeSousCategorie(String typeSousCategorie) {
        this.typeSousCategorie = typeSousCategorie;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SousCategorie createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public SousCategorie updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<Produit> produits) {
        if (this.produits != null) {
            this.produits.forEach(i -> i.setSouscategorie(null));
        }
        if (produits != null) {
            produits.forEach(i -> i.setSouscategorie(this));
        }
        this.produits = produits;
    }

    public SousCategorie produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public SousCategorie addProduit(Produit produit) {
        this.produits.add(produit);
        produit.setSouscategorie(this);
        return this;
    }

    public SousCategorie removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.setSouscategorie(null);
        return this;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        this.categorieId = categorie != null ? categorie.getId() : null;
    }

    public SousCategorie categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public Long getCategorieId() {
        return this.categorieId;
    }

    public void setCategorieId(Long categorie) {
        this.categorieId = categorie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousCategorie)) {
            return false;
        }
        return id != null && id.equals(((SousCategorie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousCategorie{" +
            "id=" + getId() +
            ", typeSousCategorie='" + getTypeSousCategorie() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
