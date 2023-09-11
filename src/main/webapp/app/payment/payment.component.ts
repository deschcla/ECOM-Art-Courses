import { Component } from '@angular/core';
import { faCreditCardAlt, faCalendarAlt } from "@fortawesome/free-solid-svg-icons";
import { Produit } from "../core/request/course.model";
import { LigneCommande } from "../core/request/ligne-commande.model";

@Component({
  selector: 'jhi-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent {
  faCreditCardAlt = faCreditCardAlt
  faCalendarAlt = faCalendarAlt

  produit1: Produit = {
    idProduit: 1,
    nomProduit: "First product" ,
    desc: "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
    tarifUnit: 12,
    quantiteDispo: 3,
    lienImg: "https://images.unsplash.com/photo-1585038021831-8afd9f9ab27f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80",
    idSousCategorie: -1,
    dateTime: new Date(),
    horaire: "",
    promo: 20,
    quantiteTotale: 10,
    idProf: -1
  }

  produit2: Produit = {
    idProduit: 2,
    nomProduit: "Second product" ,
    desc: "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
    tarifUnit: 12,
    quantiteDispo: 3,
    lienImg: "https://images.unsplash.com/photo-1522003374706-1ee629dfab6d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1134&q=80",
    idSousCategorie: -1,
    dateTime: new Date(),
    horaire: "",
    promo: 20,
    quantiteTotale: 10,
    idProf: -1
  }

  commandes: LigneCommande[] = [
    {
      idLigneCommande: 1,
      produit: this.produit1,
      quantite: 3
    },
    {
      idLigneCommande: 2,
      produit: this.produit2,
      quantite: 2
    }
  ];


  public calcMontant(): number {
      let montant = 0;
      this.commandes.forEach((commande) => {
        montant = montant + commande.produit.tarifUnit * commande.quantite
      })
    return montant
  }


}
