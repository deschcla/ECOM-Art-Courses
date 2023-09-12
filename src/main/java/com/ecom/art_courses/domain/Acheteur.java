package com.ecom.art_courses.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Acheteur.
 */
@Table("acheteur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Acheteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("adresse")
    private String adresse;

    @Column("date_naiss")
    private LocalDate dateNaiss;

    @Column("num_tel")
    private String numTel;

    @Column("created_at")
    private Instant createdAt;

    @Column("update_at")
    private Instant updateAt;

    @Transient
    private User internalUser;

    @Transient
    @JsonIgnoreProperties(value = { "commandes", "acheteur" }, allowSetters = true)
    private Set<ReleveFacture> releveFactures = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "carteBancaires", "produits", "releveFacture", "acheteur", "ligneCommandes" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    @Column("internal_user_id")
    private Long internalUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Acheteur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Acheteur adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateNaiss() {
        return this.dateNaiss;
    }

    public Acheteur dateNaiss(LocalDate dateNaiss) {
        this.setDateNaiss(dateNaiss);
        return this;
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getNumTel() {
        return this.numTel;
    }

    public Acheteur numTel(String numTel) {
        this.setNumTel(numTel);
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Acheteur createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public Acheteur updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
        this.internalUserId = user != null ? user.getId() : null;
    }

    public Acheteur internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<ReleveFacture> getReleveFactures() {
        return this.releveFactures;
    }

    public void setReleveFactures(Set<ReleveFacture> releveFactures) {
        if (this.releveFactures != null) {
            this.releveFactures.forEach(i -> i.setAcheteur(null));
        }
        if (releveFactures != null) {
            releveFactures.forEach(i -> i.setAcheteur(this));
        }
        this.releveFactures = releveFactures;
    }

    public Acheteur releveFactures(Set<ReleveFacture> releveFactures) {
        this.setReleveFactures(releveFactures);
        return this;
    }

    public Acheteur addReleveFacture(ReleveFacture releveFacture) {
        this.releveFactures.add(releveFacture);
        releveFacture.setAcheteur(this);
        return this;
    }

    public Acheteur removeReleveFacture(ReleveFacture releveFacture) {
        this.releveFactures.remove(releveFacture);
        releveFacture.setAcheteur(null);
        return this;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setAcheteur(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setAcheteur(this));
        }
        this.commandes = commandes;
    }

    public Acheteur commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Acheteur addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setAcheteur(this);
        return this;
    }

    public Acheteur removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setAcheteur(null);
        return this;
    }

    public Long getInternalUserId() {
        return this.internalUserId;
    }

    public void setInternalUserId(Long user) {
        this.internalUserId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acheteur)) {
            return false;
        }
        return id != null && id.equals(((Acheteur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Acheteur{" +
            "id=" + getId() +
            ", adresse='" + getAdresse() + "'" +
            ", dateNaiss='" + getDateNaiss() + "'" +
            ", numTel='" + getNumTel() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
