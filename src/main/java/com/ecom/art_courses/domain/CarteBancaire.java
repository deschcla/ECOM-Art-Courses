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
 * A CarteBancaire.
 */
@Table("carte_bancaire")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarteBancaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("ref_carte")
    private String refCarte;

    @Column("created_at")
    private Instant createdAt;

    @Column("update_at")
    private Instant updateAt;

    @Transient
    @JsonIgnoreProperties(value = { "carteBancaires", "produits", "releveFacture", "acheteur", "ligneCommandes" }, allowSetters = true)
    private Commande commande;

    @Column("commande_id")
    private Long commandeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarteBancaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefCarte() {
        return this.refCarte;
    }

    public CarteBancaire refCarte(String refCarte) {
        this.setRefCarte(refCarte);
        return this;
    }

    public void setRefCarte(String refCarte) {
        this.refCarte = refCarte;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CarteBancaire createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public CarteBancaire updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        this.commandeId = commande != null ? commande.getId() : null;
    }

    public CarteBancaire commande(Commande commande) {
        this.setCommande(commande);
        return this;
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
        if (!(o instanceof CarteBancaire)) {
            return false;
        }
        return id != null && id.equals(((CarteBancaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteBancaire{" +
            "id=" + getId() +
            ", refCarte='" + getRefCarte() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
