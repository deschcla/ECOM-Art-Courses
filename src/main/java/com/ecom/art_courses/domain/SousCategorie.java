package com.ecom.art_courses.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SousCategorie.
 */
@Entity
@Table(name = "sous_categorie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SousCategorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type_sous_categorie")
    private String typeSousCategorie;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @OneToMany(mappedBy = "souscategorie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "souscategorie", "ligneCommandes", "commandes" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "sousCategories" }, allowSetters = true)
    private Categorie categorie;

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
    }

    public SousCategorie categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
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

    public String getCategorieName() {
        return this.getCategorie().getTypeCategorie();
    }
}
