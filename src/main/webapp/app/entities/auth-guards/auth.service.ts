import { Injectable } from '@angular/core';
import { ILigneCommande } from '../ligne-commande/ligne-commande.model';
import { CartService } from '../../core/util/cart.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private cart: ILigneCommande[] = [];

  constructor(private cartService: CartService) {}

  public isAuthenticated(): boolean {
    this.cartService.panierChange.subscribe(value => {
      this.cart = value;
    });
    return this.cart.length !== 0;
  }
}
