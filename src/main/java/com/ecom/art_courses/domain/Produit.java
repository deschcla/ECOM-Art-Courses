package com.ecom.art_courses.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_produit")
    private String nomProduit;

    @Column(name = "jhi_desc")
    private String desc;

    @Column(name = "tarif_unit")
    private Float tarifUnit;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "duree")
    private String duree;

    @Column(name = "lien_img")
    private String lienImg;

    @Column(name = "quantite_totale")
    private Integer quantiteTotale;

    @Column(name = "quantite_dispo")
    private Integer quantiteDispo;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "nom_prof")
    private String nomProf;

    @Column(name = "promotion")
    private String promotion;

    @Column(name = "version")
    @Version
    private Integer version;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "produits", "categorie" }, allowSetters = true)
    private SousCategorie souscategorie;

    @OneToMany(mappedBy = "produit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "produit", "commande" }, allowSetters = true)
    private Set<LigneCommande> ligneCommandes = new HashSet<>();

    @ManyToMany(mappedBy = "produits")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carteBancaires", "produits", "releveFacture", "acheteur", "ligneCommandes" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Produit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProduit() {
        return this.nomProduit;
    }

    public Produit nomProduit(String nomProduit) {
        this.setNomProduit(nomProduit);
        return this;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDesc() {
        return this.desc;
    }

    public Produit desc(String desc) {
        this.setDesc(desc);
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Float getTarifUnit() {
        return this.tarifUnit;
    }

    public Produit tarifUnit(Float tarifUnit) {
        this.setTarifUnit(tarifUnit);
        return this;
    }

    public void setTarifUnit(Float tarifUnit) {
        this.tarifUnit = tarifUnit;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Produit date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getDuree() {
        return this.duree;
    }

    public Produit duree(String duree) {
        this.setDuree(duree);
        return this;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getLienImg() {
        return this.lienImg;
    }

    public Produit lienImg(String lienImg) {
        this.setLienImg(lienImg);
        return this;
    }

    public void setLienImg(String lienImg) {
        this.lienImg = lienImg;
    }

    public Integer getQuantiteTotale() {
        return this.quantiteTotale;
    }

    public Produit quantiteTotale(Integer quantiteTotale) {
        this.setQuantiteTotale(quantiteTotale);
        return this;
    }

    public void setQuantiteTotale(Integer quantiteTotale) {
        this.quantiteTotale = quantiteTotale;
    }

    public Integer getQuantiteDispo() {
        return this.quantiteDispo;
    }

    public Produit quantiteDispo(Integer quantiteDispo) {
        this.setQuantiteDispo(quantiteDispo);
        return this;
    }

    public void setQuantiteDispo(Integer quantiteDispo) {
        this.quantiteDispo = quantiteDispo;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Produit createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public Produit updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public String getNomProf() {
        return this.nomProf;
    }

    public Produit nomProf(String nomProf) {
        this.setNomProf(nomProf);
        return this;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProduit;
    }

    public String getPromotion() {
        return this.promotion;
    }

    public Produit promotion(String promotion) {
        this.setPromotion(promotion);
        return this;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Produit version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public SousCategorie getSouscategorie() {
        return this.souscategorie;
    }

    public void setSouscategorie(SousCategorie sousCategorie) {
        this.souscategorie = sousCategorie;
    }

    public Produit souscategorie(SousCategorie sousCategorie) {
        this.setSouscategorie(sousCategorie);
        return this;
    }

    public Set<LigneCommande> getLigneCommandes() {
        return this.ligneCommandes;
    }

    public void setLigneCommandes(Set<LigneCommande> ligneCommandes) {
        if (this.ligneCommandes != null) {
            this.ligneCommandes.forEach(i -> i.setProduit(null));
        }
        if (ligneCommandes != null) {
            ligneCommandes.forEach(i -> i.setProduit(this));
        }
        this.ligneCommandes = ligneCommandes;
    }

    public Produit ligneCommandes(Set<LigneCommande> ligneCommandes) {
        this.setLigneCommandes(ligneCommandes);
        return this;
    }

    public Produit addLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.add(ligneCommande);
        ligneCommande.setProduit(this);
        return this;
    }

    public Produit removeLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.remove(ligneCommande);
        ligneCommande.setProduit(null);
        return this;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.removeProduit(this));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.addProduit(this));
        }
        this.commandes = commandes;
    }

    public Produit commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Produit addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.getProduits().add(this);
        return this;
    }

    public Produit removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.getProduits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", nomProduit='" + getNomProduit() + "'" +
            ", desc='" + getDesc() + "'" +
            ", tarifUnit=" + getTarifUnit() +
            ", date='" + getDate() + "'" +
            ", duree='" + getDuree() + "'" +
            ", lienImg='" + getLienImg() + "'" +
            ", quantiteTotale=" + getQuantiteTotale() +
            ", quantiteDispo=" + getQuantiteDispo() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", nomProf='" + getNomProf() + "'" +
            ", promotion='" + getPromotion() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
