import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from 'app/core/util/cart.service';
import { NotificationService } from 'app/core/util/notification.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { IReleveFacture } from 'app/entities/releve-facture/releve-facture.model';

@Component({
  selector: 'jhi-facture',
  templateUrl: './facture.component.html',
  styleUrls: ['./facture.component.scss'],
})
export class FactureComponent implements OnInit {
  commandes: ILigneCommande[] = [];
  releveFacture: IReleveFacture;
  date: Date;

  constructor(private cartService: CartService, private router: Router, private ntfService: NotificationService) {}

  ngOnInit(): void {
    this.commandes = this.cartService.cart;
    this.releveFacture = window.history.state;
    this.cartService.counterChange.next(0);
    this.cartService.cart = [];
    this.date = new Date();
    this.ntfService.notifyBanner('Success', 'Achat finalisé!');
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

  returnHome(): void {
    this.router.navigateByUrl('');
  }

  print(): void {
    setTimeout(function () {
      window.print();
    }, 1000);
  }
}
