import { Component } from '@angular/core';
import { faCreditCardAlt, faCalendarAlt, faLock } from "@fortawesome/free-solid-svg-icons";
import { Produit } from "../core/request/course.model";
import { LigneCommande } from "../core/request/ligne-commande.model";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'jhi-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent{
  faCreditCardAlt = faCreditCardAlt
  faCalendarAlt = faCalendarAlt
  faLock = faLock


  paymentForm = new FormGroup({
    card_number: new FormControl(null, [
      Validators.required,
      Validators.pattern(/^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\d{3})\d{11})$/)
    ]),
    date_exp: new FormControl(null, [
      Validators.required,
      Validators.pattern(/^(0[1-9]|1[0-2])\/?([0-9]{4}|[0-9]{2})$/)
    ]),
    ccv: new FormControl(null, [
      Validators.required,
      Validators.pattern(/^[0-9]{3,4}$/)
    ])
  });


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
    tarifUnit: 8,
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

  public validatePayment(): void {
    alert(this.paymentForm.status)
  }

}
