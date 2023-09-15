import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  counter: number = 0;
  cart: ILigneCommande[] = [];
  // ligneCommande: NewLigneCommande;

  counterChange: Subject<number> = new Subject<number>();

  // constructor(private ligneCommandeService: LigneCommandeService) {}

  // addToCart(course: IProduit, num: number): void {
  //   this.ligneCommande = {
  //     id: null,
  //     quantite: num,
  //     montant: course.tarifUnit,
  //     validated: 0,
  //     produit: course,
  //   };
  //   this.ligneCommandeService.create(this.ligneCommande).subscribe({
  //     next: value => {
  //       console.log('added to cart');
  //       this.counterChange.next((this.counter += num));
  //     },
  //     error: error => console.log(error),
  //   });
  // }

  addToCart(course: IProduit, num: number): void {
    this.cart.push({
      id: -1,
      produit: course,
      quantite: num,
    });
    this.counterChange.next((this.counter += num));
  }

  deleteCartProducts(commandeChoosen: ILigneCommande): void {

    this.cart.forEach((commande, index) => {
      if(commandeChoosen == commande){
        this.cart.splice(index,1)
        this.counterChange.next((this.counter -= commande.quantite!));
      }
    })
  }

  getCartProducts(): ILigneCommande[] {
    return this.cart;
  }
}
