import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { AccountService } from '../auth/account.service';
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
  // ligneCommande: NewLigneCommande;

  counterChange: Subject<number> = new Subject<number>();
  searchChange: Subject<string> = new Subject<string>();

  constructor(
    private ligneCommandeService: LigneCommandeService,
    private accountService: AccountService,
    private ntfService: NotificationService
  ) {}

  addToCart(course: IProduit, num: number): void {
    // this.cart.push({
    //   id: -1,
    //   produit: course,
    //   quantite: num,
    // });
    this.accountService
      .identity()
      .pipe()
      .subscribe(account => (this.account = account));

    this.ligneCommandeService
      .create({
        id: null,
        quantite: num,
        montant: course.tarifUnit! * num,
        validated: 0,
        nomParticipant: this.account?.firstName + ' ' + this.account?.lastName,
        createdAt: dayjs(),
        updateAt: dayjs(),
        produit: course,
        commande: {
          id: this.account?.id ? this.account.id : -1,
        },
      })
      .subscribe({
        next: value => this.counterChange.next((this.counter += num)),
        error: error => console.log(error),
      });
  }

  changeToCart(course: ILigneCommande, num: number): void {
    this.cart.forEach(commande => {
      if (course == commande.produit) {
        this.counterChange.next((this.counter = this.counter - commande.quantite! + num));
        commande.quantite = num;
      }
    });
  }

  deleteCartProducts(commandeChoosen: ILigneCommande): void {
    this.cart.forEach((item, index) => {
      if (item.id === commandeChoosen.id) {
        this.cart.splice(index, 1);
        this.counterChange.next((this.counter -= commandeChoosen.quantite!));
      }
    });
    this.ntfService.notifyBanner('Success', 'Produit ajouté au panier avec succès');
  }

  getCartProducts(): ILigneCommande[] {
    let res: ILigneCommande[] = [];
    this.cart.forEach(commande => {
      if (commande.produit?.quantiteDispo == 0) {
        this.deleteCartProducts(commande);
      }
      res = this.cart;
    });
    return res;
  }

  fillCourses(courses: IProduit[]): void {
    this.courses = courses;
    this.courseChange.next(this.courses);
  }

  setSearchValue(value: string): void {
    this.searchChange.next(value);
  }
}
