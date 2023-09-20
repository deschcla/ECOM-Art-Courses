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
  courses: IProduit[] = [];
  courseChange: Subject<IProduit[]> = new Subject<IProduit[]>();
  // ligneCommande: NewLigneCommande;

  counterChange: Subject<number> = new Subject<number>();
  searchChange: Subject<string> = new Subject<string>();

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

  getCartProducts(): ILigneCommande[] {
    return this.cart;
  }

  fillCourses(courses: IProduit[]): void {
    this.courses = courses;
    this.courseChange.next(this.courses);
  }

  // searchValue: string = '';

  setSearchValue(value: string): void {
    console.log(value);
    // this.searchValue = value;
    this.searchChange.next(value);
  }
  // getSearchValue(): string{
  //   return this.searchValue;
  // }
}
