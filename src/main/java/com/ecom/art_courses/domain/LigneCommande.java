package com.ecom.art_courses.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A LigneCommande.
 */
@Table("ligne_commande")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("quantite")
    private Integer quantite;

    @Column("montant")
    private Float montant;

    @Column("valided")
    private Integer valided;

    @Column("created_at")
    private Instant createdAt;

    @Column("update_at")
    private Instant updateAt;

    @Transient
    @JsonIgnoreProperties(value = { "souscategorie", "ligneCommandes", "commandes" }, allowSetters = true)
    private Produit produit;

    @Transient
    @JsonIgnoreProperties(value = { "carteBancaires", "produits", "releveFacture", "acheteur", "ligneCommandes" }, allowSetters = true)
    private Commande commande;

    @Column("produit_id")
    private Long produitId;

    @Column("commande_id")
    private Long commandeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LigneCommande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public LigneCommande quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Float getMontant() {
        return this.montant;
    }

    public LigneCommande montant(Float montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Integer getValided() {
        return this.valided;
    }

    public LigneCommande valided(Integer valided) {
        this.setValided(valided);
        return this;
    }

    public void setValided(Integer valided) {
        this.valided = valided;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public LigneCommande createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public LigneCommande updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        this.produitId = produit != null ? produit.getId() : null;
    }

    public LigneCommande produit(Produit produit) {
        this.setProduit(produit);
        return this;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        this.commandeId = commande != null ? commande.getId() : null;
    }

    public LigneCommande commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public Long getProduitId() {
        return this.produitId;
    }

    public void setProduitId(Long produit) {
        this.produitId = produit;
    }

    public Long getCommandeId() {
        return this.commandeId;
    }

    public void setCommandeId(Long commande) {
        this.commandeId = commande;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneCommande)) {
            return false;
        }
        return id != null && id.equals(((LigneCommande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneCommande{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", montant=" + getMontant() +
            ", valided=" + getValided() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
