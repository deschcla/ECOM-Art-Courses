import { Component, OnInit } from '@angular/core';
import { faCreditCardAlt, faCalendarAlt, faLock } from '@fortawesome/free-solid-svg-icons';
import { FormControl, FormGroup, Validators, ValidatorFn } from '@angular/forms';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { CartService } from 'app/core/util/cart.service';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { NewReleveFacture } from 'app/entities/releve-facture/releve-facture.model';
import dayjs from 'dayjs/esm';
import { AccountService } from 'app/core/auth/account.service';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { ReleveFactureService } from 'app/entities/releve-facture/service/releve-facture.service';

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
      validDateExp,
    ]),
    ccv: new FormControl(null, [Validators.required, Validators.pattern(/^[0-9]{3,4}$/)]),
  });

  quantite: number = 0;

  commandes: ILigneCommande[] = [];

  constructor(
    private cartService: CartService,
    private router: Router,
    private titleService: Title,
    private translateService: TranslateService,
    private accountService: AccountService,
    private ligneCommandeService: LigneCommandeService,
    private produitService: ProduitService,
    private releveFactureService: ReleveFactureService
  ) {}

  ngOnInit(): void {
    this.translateService.get('payment.title').subscribe(title => this.titleService.setTitle(title));
    this.commandes = this.cartService.cart;
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
    this.accountService.identity().subscribe({
      next: account => {
        const releveFacture: NewReleveFacture = {
          id: null,
          montant: +this.calcMontant().toFixed(2),
          createdAt: dayjs(),
          updateAt: dayjs(),
          user: {
            id: account?.id ? account.id : -1,
            firstName: account?.firstName ? account.firstName : undefined,
            lastName: account?.lastName ? account.lastName : undefined,
            email: account?.email ? account.email : undefined,
          },
        };
        this.commandes.forEach(commande => {
          commande.validated = 1;
          commande.updateAt = dayjs();
          this.ligneCommandeService.update(commande).subscribe({
            next: () => {
              commande.produit!.quantiteDispo = commande.produit!.quantiteDispo! - commande.quantite!;
              const dateOld = commande.produit?.date;
              const createdAtOld = commande.produit?.createdAt;
              const updateAtOld = commande.produit?.updateAt;
              commande.produit!.date = dayjs(dateOld);
              commande.produit!.createdAt = dayjs(createdAtOld);
              commande.produit!.updateAt = dayjs(updateAtOld);
              this.produitService.find(commande.produit!.id).subscribe({
                next: value => {
                  commande.produit!['souscategorie'] = value.body?.souscategorie ? value.body.souscategorie : null;
                  this.produitService.update(commande.produit!).subscribe();
                },
              });
            },
          });
        });
        this.releveFactureService.create(releveFacture).subscribe({
          next: val => {
            this.router.navigateByUrl('/facture', { state: val.body! });
          },
        });
      },
    });
  }

  viewDetails(courseId: number): void {
    this.router.navigateByUrl('/course-details/' + courseId.toString());
  }
}

export const validDateExp: ValidatorFn = control => {
  let res = false;
  // year input > year actuel
  if (control.value.split('/')[1] > new Date().getFullYear().toString().substring(2, 4)) {
    res = true;
    // year input == year actuel
  } else if (control.value.split('/')[1] === new Date().getFullYear().toString().substring(2, 4)) {
    //month input < month actuel
    if (+control.value.split('/')[0] < +new Date().toLocaleDateString().split('/')[0]) {
      res = false;
      // month input >= month actuel
    } else {
      res = true;
    }
  } else {
    res = false;
  }
  if (!res) {
    return {
      validDateExp: {
        reason: '',
        value: control.value,
      },
    };
  } else {
    return null;
  }
};
