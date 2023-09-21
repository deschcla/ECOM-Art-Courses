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
    this.cart = this.cartService.getCartProducts();
    return this.cart.length !== 0;
  }
}
