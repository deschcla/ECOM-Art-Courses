package com.ecom.art_courses.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LigneCommande.
 */
@Entity
@Table(name = "ligne_commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LigneCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "montant")
    private Float montant;

    @Column(name = "validated")
    private Integer validated;

    @Column(name = "nom_participant")
    private String nomParticipant;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "souscategorie", "ligneCommandes", "commandes" }, allowSetters = true)
    private Produit produit;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "carteBancaires", "produits", "releveFacture", "acheteur", "ligneCommandes" }, allowSetters = true)
    private Commande commande;

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

    public Integer getValidated() {
        return this.validated;
    }

    public LigneCommande validated(Integer validated) {
        this.setValidated(validated);
        return this;
    }

    public void setValidated(Integer validated) {
        this.validated = validated;
    }

    public String getNomParticipant() {
        return this.nomParticipant;
    }

    public LigneCommande nomParticipant(String nomParticipant) {
        this.setNomParticipant(nomParticipant);
        return this;
    }

    public void setNomParticipant(String nomParticipant) {
        this.nomParticipant = nomParticipant;
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
    }

    public LigneCommande commande(Commande commande) {
        this.setCommande(commande);
        return this;
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
            ", validated=" + getValidated() +
            ", nomParticipant='" + getNomParticipant() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
