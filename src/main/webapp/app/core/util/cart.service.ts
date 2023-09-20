import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { NotificationService } from './notification.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  counter: number = 0;
  cart: ILigneCommande[] = [];
  courses: IProduit[] = [];
  courseChange: Subject<IProduit[]> = new Subject<IProduit[]>();
  // ligneCommande: NewLigneCommande;

  counterChange: Subject<number> = new Subject<number>();
  searchChange: Subject<string> = new Subject<string>();

  constructor(private ntfService: NotificationService) {}

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
    this.ntfService.notifyBanner('Success', 'Produit ajouté au panier avec succès');
  }

  getCartProducts(): ILigneCommande[] {
    return this.cart;
  }

  fillCourses(courses: IProduit[]): void {
    this.courses = courses;
    this.courseChange.next(this.courses);
  }

  setSearchValue(value: string): void {
    this.searchChange.next(value);
  }
}
