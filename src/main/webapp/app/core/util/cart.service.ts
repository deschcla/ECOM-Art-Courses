import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { NewLigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  counter: number = 0;
  cart: NewLigneCommande[] = [];
  ligneCommande: NewLigneCommande;

  counterChange: Subject<number> = new Subject<number>();

  constructor(private ligneCommandeService: LigneCommandeService) {}

  addToCart(course: IProduit, num: number): void {
    for (let i = 0; i < num; i++) {
      this.ligneCommande = {
        id: null,
        quantite: num,
        montant: course.tarifUnit,
        validated: 0,
        produit: course,
      };
      this.cart.push(this.ligneCommande);
      this.ligneCommandeService.create(this.ligneCommande).subscribe({
        next: value => {
          console.log('added to cart');
          this.counterChange.next((this.counter += num));
        },
        error: error => console.log(error),
      });
    }
  }
}
