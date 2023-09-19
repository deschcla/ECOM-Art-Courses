import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { AccountService } from '../auth/account.service';
import { Account } from '../auth/account.model';
import dayjs from 'dayjs/esm';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  counter: number = 0;
  cart: ILigneCommande[] = [];
  account: Account | null;
  // ligneCommande: NewLigneCommande;

  counterChange: Subject<number> = new Subject<number>();

  constructor(private ligneCommandeService: LigneCommandeService, private accountService: AccountService) {}

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

  changeToCart(course: IProduit, num: number): void {
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
}
