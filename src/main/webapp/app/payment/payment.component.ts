import { Component, OnInit } from '@angular/core';
import { faCreditCardAlt, faCalendarAlt, faLock } from '@fortawesome/free-solid-svg-icons';
import { FormControl, FormGroup, Validators, ValidatorFn } from '@angular/forms';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { CartService } from 'app/core/util/cart.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss'],
})
export class PaymentComponent implements OnInit {
  faCreditCardAlt = faCreditCardAlt;
  faCalendarAlt = faCalendarAlt;
  faLock = faLock;
  model_date_expiration: string = '';

  paymentForm = new FormGroup({
    card_number: new FormControl(null, [
      Validators.required,
      Validators.pattern(
        /^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\d{3})\d{11})$/
      ),
    ]),
    date_exp: new FormControl(this.model_date_expiration, [
      Validators.required,
      Validators.pattern(/^(0[1-9]|1[0-2])\/?([0-9]{4}|[0-9]{2})$/),
      validDateExp
      ]
    ),
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
    alert(this.paymentForm.status);
  }

  viewDetails(courseId: number): void {
    this.router.navigateByUrl('/course-details/' + courseId.toString());
  }
}

export const validDateExp: ValidatorFn = (control) =>{
  let res = false

  // year input > year actuel
  if(control.value.split('/')[1] > (new Date().getFullYear()).toString().substring(2, 4)){
    res = true
    // year input == year actuel
  }else if(control.value.split('/')[1] === (new Date().getFullYear()).toString().substring(2, 4)){
    //month input < month actuel
    if((control.value.split('/')[0]) < (new Date().toLocaleDateString().split('/')[1])){
      res = false
      // month input >= month actuel
    }else{
      res = true
    }
  }else{
    res = false
  }
  if(!res){
    return {
      'validDateExp': {
        reason: '',
        value: control.value
      }
    };
  }else{
    return null
  }

}
