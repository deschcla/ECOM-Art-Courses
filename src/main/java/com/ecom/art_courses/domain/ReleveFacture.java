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
 * A ReleveFacture.
 */
@Table("releve_facture")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReleveFacture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("montant")
    private Float montant;

    @Column("created_at")
    private Instant createdAt;

    @Column("update_at")
    private Instant updateAt;

    @Transient
    @JsonIgnoreProperties(value = { "carteBancaires", "produits", "releveFacture", "acheteur", "ligneCommandes" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "internalUser", "releveFactures", "commandes" }, allowSetters = true)
    private Acheteur acheteur;

    @Column("acheteur_id")
    private Long acheteurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReleveFacture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMontant() {
        return this.montant;
    }

    public ReleveFacture montant(Float montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ReleveFacture createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public ReleveFacture updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setReleveFacture(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setReleveFacture(this));
        }
        this.commandes = commandes;
    }

    public ReleveFacture commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public ReleveFacture addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setReleveFacture(this);
        return this;
    }

    public ReleveFacture removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setReleveFacture(null);
        return this;
    }

    public Acheteur getAcheteur() {
        return this.acheteur;
    }

    public void setAcheteur(Acheteur acheteur) {
        this.acheteur = acheteur;
        this.acheteurId = acheteur != null ? acheteur.getId() : null;
    }

    public ReleveFacture acheteur(Acheteur acheteur) {
        this.setAcheteur(acheteur);
        return this;
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
        if (!(o instanceof ReleveFacture)) {
            return false;
        }
        return id != null && id.equals(((ReleveFacture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleveFacture{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
