import { Component, OnInit } from '@angular/core';
import { CartService } from 'app/core/util/cart.service';
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
  date: string;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.commandes = this.cartService.getCartProducts();
    this.releveFacture = window.history.state;
    this.date = '12/12/2012';
    console.log(this.releveFacture);
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
}
