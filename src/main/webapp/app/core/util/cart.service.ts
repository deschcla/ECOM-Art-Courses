import { Injectable } from '@angular/core';
import { Observable, Subject, map } from 'rxjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { AccountService } from '../auth/account.service';
import { Account } from '../auth/account.model';
import dayjs from 'dayjs/esm';
import { NotificationService } from './notification.service';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { HttpResponse } from '@angular/common/http';

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
  accountChange: Subject<Account> = new Subject<Account>();

  constructor(
    private ligneCommandeService: LigneCommandeService,
    private accountService: AccountService,
    private ntfService: NotificationService,
    private commandeService: CommandeService
  ) {}

  getUser(): Subject<Account> {
    if (this.account == null) {
      this.accountService
        .identity()
        .pipe()
        .subscribe(account => {
          console.log(account);
          this.account = account;
          this.accountChange.next(this.account!);
        });
    }
    return this.accountChange;
  }

  addToCart(course: IProduit, num: number): void {
    this.getUser().subscribe({
      next: () => {
        this.ligneCommandeService
          .create({
            id: null,
            quantite: num,
            montant: course.tarifUnit! * num,
            validated: 0,
            nomParticipant: this.account?.firstName?.concat(' ', this.account.lastName ? this.account.lastName : ''),
            createdAt: dayjs(),
            updateAt: dayjs(),
            produit: course,
            commande: {
              id: this.account?.id ? this.account.id : -1,
            },
          })
          .subscribe({
            next: () => this.counterChange.next((this.counter += num)),
            error: error => console.log(error),
            complete: () => this.ntfService.notifyBanner('Success', 'Produit ajouté au panier avec succès'),
          });
      },
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
            this.counterChange.next((this.counter = this.counter - commande.quantite! + num));
            commande.quantite = num;
          }
        });
      },
      error: err => console.log(err),
      complete: () => {
        if (num === 0) {
          this.ntfService.notifyBanner('Success', 'item deleted');
        } else {
          this.ntfService.notifyBanner('Success', 'item quantity updated to ' + num.toString());
        }
      },
    });
  }

  deleteCartProducts(commandeChoosen: ILigneCommande): void {
    this.changeToCart(commandeChoosen, 0);
    // this.ligneCommandeService.delete(commandeChoosen).subscribe({
    //   next: value=>console.log("deleted"),
    //   error: err => console.log(err)
    // })
    // this.cart.forEach((item, index) => {
    //   if (item.id === commandeChoosen.id) {
    //     this.cart.splice(index, 1);
    //     this.counterChange.next((this.counter -= commandeChoosen.quantite!));
    //   }
    // });
  }

  getCartProducts(): Subject<ILigneCommande[]> {
    // let res: ILigneCommande[] = [];
    // this.cart.forEach(commande => {
    //   if (commande.produit?.quantiteDispo == 0) {
    //     this.deleteCartProducts(commande);
    //   }
    //   res = this.cart;
    // });
    // return res;
    this.getUser().subscribe({
      next: () => {
        if (this.account?.id) {
          this.commandeService
            .getPanier(this.account.id)
            .pipe(map((res: HttpResponse<ILigneCommande[]>) => res.body ?? []))

            .subscribe({
              next: (value: ILigneCommande[]) => {
                this.cart = value;
                this.panierChange.next(this.cart);
              },
              complete: () => {
                this.calculateQuantity();
              },
            });
        }
      },
    });
    console.log(this.cart);
    return this.panierChange;
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
