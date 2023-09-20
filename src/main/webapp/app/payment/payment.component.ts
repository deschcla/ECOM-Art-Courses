import { Component, OnInit } from '@angular/core';
import { faCreditCardAlt, faCalendarAlt, faLock } from '@fortawesome/free-solid-svg-icons';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { CartService } from 'app/core/util/cart.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { Router } from '@angular/router';
import { IReleveFacture } from 'app/entities/releve-facture/releve-facture.model';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss'],
})
export class PaymentComponent implements OnInit {
  faCreditCardAlt = faCreditCardAlt;
  faCalendarAlt = faCalendarAlt;
  faLock = faLock;

  paymentForm = new FormGroup({
    card_number: new FormControl(null, [
      Validators.required,
      Validators.pattern(
        /^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\d{3})\d{11})$/
      ),
    ]),
    date_exp: new FormControl(null, [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/?([0-9]{4}|[0-9]{2})$/)]),
    ccv: new FormControl(null, [Validators.required, Validators.pattern(/^[0-9]{3,4}$/)]),
  });

  quantite: number = 0;

  commandes: ILigneCommande[] = [];

  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit(): void {
    this.commandes = this.cartService.getCartProducts();
    this.commandes.forEach(commande => (this.quantite += commande.quantite != null ? commande.quantite : 0));
  }

  public calcMontant(): number {
    let montant = 0;
    this.commandes.forEach(commande => {
      // if(commande.produit?.promo != null){
      //   montant +=  (commande.produit.tarifUnit - ((commande.produit.tarifUnit * commande.produit.promo) / 100)) * commande.quantite
      // }else{
      montant =
        montant +
        (commande.produit?.tarifUnit != null ? commande.produit.tarifUnit : 0) * (commande.quantite != null ? commande.quantite : 0);
      // }
    });
    return montant;
  }

  public validatePayment(): void {
    // alert(this.paymentForm.status);
    const releveFacture: IReleveFacture = {
      id: 1,
      montant: +(this.calcMontant() * 1.2).toFixed(2),
      createdAt: dayjs('2023-09-12T08:00:22Z'),
      acheteur: {
        id: 1,
        internalUser: {
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
          email: 'john@doe.com',
          numTel: '+33769289876',
        },
      },
    };
    console.log(releveFacture);
    this.router.navigateByUrl('/facture', { state: releveFacture });
  }

  viewDetails(courseId: number): void {
    this.router.navigateByUrl('/course-details/' + courseId.toString());
  }
}
