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
 * A Commande.
 */
@Table("commande")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("montant")
    private Float montant;

    @Column("valided")
    private Integer valided;

    @Column("created_at")
    private Instant createdAt;

    @Column("update_at")
    private Instant updateAt;

    @Transient
    @JsonIgnoreProperties(value = { "commande" }, allowSetters = true)
    private Set<CarteBancaire> carteBancaires = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "souscategorie", "ligneCommandes", "commandes" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commandes", "acheteur" }, allowSetters = true)
    private ReleveFacture releveFacture;

    @Transient
    @JsonIgnoreProperties(value = { "internalUser", "releveFactures", "commandes" }, allowSetters = true)
    private Acheteur acheteur;

    @Transient
    @JsonIgnoreProperties(value = { "produit", "commande" }, allowSetters = true)
    private Set<LigneCommande> ligneCommandes = new HashSet<>();

    @Column("releve_facture_id")
    private Long releveFactureId;

    @Column("acheteur_id")
    private Long acheteurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMontant() {
        return this.montant;
    }

    public Commande montant(Float montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Integer getValided() {
        return this.valided;
    }

    public Commande valided(Integer valided) {
        this.setValided(valided);
        return this;
    }

    public void setValided(Integer valided) {
        this.valided = valided;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Commande createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public Commande updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Set<CarteBancaire> getCarteBancaires() {
        return this.carteBancaires;
    }

    public void setCarteBancaires(Set<CarteBancaire> carteBancaires) {
        if (this.carteBancaires != null) {
            this.carteBancaires.forEach(i -> i.setCommande(null));
        }
        if (carteBancaires != null) {
            carteBancaires.forEach(i -> i.setCommande(this));
        }
        this.carteBancaires = carteBancaires;
    }

    public Commande carteBancaires(Set<CarteBancaire> carteBancaires) {
        this.setCarteBancaires(carteBancaires);
        return this;
    }

    public Commande addCarteBancaire(CarteBancaire carteBancaire) {
        this.carteBancaires.add(carteBancaire);
        carteBancaire.setCommande(this);
        return this;
    }

    public Commande removeCarteBancaire(CarteBancaire carteBancaire) {
        this.carteBancaires.remove(carteBancaire);
        carteBancaire.setCommande(null);
        return this;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public Commande produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public Commande addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getCommandes().add(this);
        return this;
    }

    public Commande removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getCommandes().remove(this);
        return this;
    }

    public ReleveFacture getReleveFacture() {
        return this.releveFacture;
    }

    public void setReleveFacture(ReleveFacture releveFacture) {
        this.releveFacture = releveFacture;
        this.releveFactureId = releveFacture != null ? releveFacture.getId() : null;
    }

    public Commande releveFacture(ReleveFacture releveFacture) {
        this.setReleveFacture(releveFacture);
        return this;
    }

    public Acheteur getAcheteur() {
        return this.acheteur;
    }

    public void setAcheteur(Acheteur acheteur) {
        this.acheteur = acheteur;
        this.acheteurId = acheteur != null ? acheteur.getId() : null;
    }

    public Commande acheteur(Acheteur acheteur) {
        this.setAcheteur(acheteur);
        return this;
    }

    public Set<LigneCommande> getLigneCommandes() {
        return this.ligneCommandes;
    }

    public void setLigneCommandes(Set<LigneCommande> ligneCommandes) {
        if (this.ligneCommandes != null) {
            this.ligneCommandes.forEach(i -> i.setCommande(null));
        }
        if (ligneCommandes != null) {
            ligneCommandes.forEach(i -> i.setCommande(this));
        }
        this.ligneCommandes = ligneCommandes;
    }

    public Commande ligneCommandes(Set<LigneCommande> ligneCommandes) {
        this.setLigneCommandes(ligneCommandes);
        return this;
    }

    public Commande addLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.add(ligneCommande);
        ligneCommande.setCommande(this);
        return this;
    }

    public Commande removeLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.remove(ligneCommande);
        ligneCommande.setCommande(null);
        return this;
    }

    public Long getReleveFactureId() {
        return this.releveFactureId;
    }

    public void setReleveFactureId(Long releveFacture) {
        this.releveFactureId = releveFacture;
    }

    public Long getAcheteurId() {
        return this.acheteurId;
    }

    public void setAcheteurId(Long acheteur) {
        this.acheteurId = acheteur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", valided=" + getValided() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
