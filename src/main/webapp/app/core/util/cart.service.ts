import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { Account } from '../auth/account.model';
import dayjs from 'dayjs/esm';
import { NotificationService } from './notification.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  counter: number = 0;
  cart: ILigneCommande[] = [];
  account: Account | null;
  courses: IProduit[] = [];
  courseChange: Subject<IProduit[]> = new Subject<IProduit[]>();
  panierChange: Subject<ILigneCommande[]> = new Subject<ILigneCommande[]>();
  counterChange: Subject<number> = new Subject<number>();
  searchChange: Subject<string> = new Subject<string>();
  accountChange: Subject<Account | null> = new Subject<Account | null>();

  constructor(private ligneCommandeService: LigneCommandeService, private ntfService: NotificationService) {}

  addToCart(course: IProduit, num: number, account: Account): void {
    const newLigneCommande = {
      id: null,
      quantite: num,
      montant: course.tarifUnit! * num,
      validated: 0,
      nomParticipant: account.firstName?.concat(' ', account.lastName ? account.lastName : ''),
      createdAt: dayjs(),
      updateAt: dayjs(),
      produit: course,
      commande: {
        id: account.id ? account.id : -1,
      },
    };
    this.ligneCommandeService.create(newLigneCommande).subscribe({
      next: value => {
        this.cart.push(value.body!);
        this.counterChange.next((this.counter += num));
        this.panierChange.next(this.cart);
      },
      error: error => this.ntfService.notifyBanner('Error', error),
      complete: () => this.ntfService.notifyBanner('Success', 'Produit ajouté au panier avec succès'),
    });
  }

  changeToCart(course: ILigneCommande, num: number): void {
    course.quantite = num;
    course.updateAt = dayjs();
    const createdAtOld = course.createdAt;
    course.createdAt = dayjs(createdAtOld);

    this.ligneCommandeService.update(course).subscribe({
      next: () => {
        this.cart.forEach(commande => {
          if (course.produit === commande.produit) {
            this.calculateQuantity();
            commande.quantite = num;
          }
        });
      },
      error: err => this.ntfService.notifyBanner('Error', err),
      complete: () => {
        if (num === 0) {
          this.ntfService.notifyBanner('Success', 'Ce produit a été supprimé de votre panier.');
        } else {
          this.ntfService.notifyBanner('Success', 'La quantité de ce produit a été mise à jour à ' + num.toString());
        }
      },
    });
  }

  deleteCartProducts(commandeChoosen: ILigneCommande): void {
    this.changeToCart(commandeChoosen, 0);
    this.cart.forEach((item, index) => {
      if (item.id === commandeChoosen.id) {
        this.cart.splice(index, 1);
        this.calculateQuantity();
      }
    });
  }

  getCartProducts(): ILigneCommande[] {
    return this.cart;
  }

  calculateQuantity(): void {
    this.counter = 0;
    this.cart.forEach(commande => (this.counter += commande.quantite!));
    this.counterChange.next(this.counter);
  }

  fillCourses(courses: IProduit[]): void {
    this.courses = courses;
    this.courseChange.next(this.courses);
  }

  setSearchValue(value: string): void {
    this.searchChange.next(value);
  }
}
