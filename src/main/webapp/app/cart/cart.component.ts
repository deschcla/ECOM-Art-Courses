import { Component, OnInit } from '@angular/core';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { CartService } from 'app/core/util/cart.service';
import { Router } from '@angular/router';
import { IProduit } from 'app/entities/produit/produit.model';
import { faTrashCan } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'jhi-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit{

  quantite: number = 0;
  commandes: ILigneCommande[] = [];
  selectedAmount: number = 1;
  course: IProduit | null = null;
  faTrashCan = faTrashCan;

  constructor(
    private cartService: CartService,
    private router: Router,
    ) {
  }

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

  viewDetails(courseId: number): void {
    this.router.navigateByUrl('/course-details/' + courseId.toString());
  }

  onSelected(value: string): void {
    this.selectedAmount = +value;
  }

  counter(i: number): any[] {
    return new Array(i);
  }

  removeILigneCommande(commandeChoosen: ILigneCommande): void{
    /*
    this.commandes.forEach((commande, index) => {
      if(commandeChoosen == commande){
        this.commandes.splice(index,1)
      }
    })

     */
    this.cartService.deleteCartProducts(commandeChoosen)
  }

}
