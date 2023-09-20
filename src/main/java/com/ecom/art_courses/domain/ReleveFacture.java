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
 * A ReleveFacture.
 */
@Entity
@Table(name = "releve_facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReleveFacture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "montant")
    private Float montant;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @OneToMany(mappedBy = "releveFacture")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carteBancaires", "produits", "releveFacture", "user", "ligneCommandes" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "internalUser", "releveFactures", "commandes" }, allowSetters = true)
    private User user;

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

    //    public Acheteur getAcheteur() {
    //        return this.acheteur;
    //    }
    //
    //    public void setAcheteur(Acheteur acheteur) {
    //        this.acheteur = acheteur;
    //    }
    //
    //    public ReleveFacture acheteur(Acheteur acheteur) {
    //        this.setAcheteur(acheteur);
    //        return this;
    //    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReleveFacture user(User user) {
        this.setUser(user);
        return this;
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
