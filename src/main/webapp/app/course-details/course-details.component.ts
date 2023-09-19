import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { Subject, takeUntil } from 'rxjs';
import { CartService } from 'app/core/util/cart.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { LoginService } from '../login/login.service';
import { SousCategorieService } from 'app/entities/sous-categorie/service/sous-categorie.service';

@Component({
  selector: 'jhi-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.scss'],
})
export class CourseDetailsComponent implements OnInit, OnDestroy {
  idProduit: number;
  course: IProduit | null = null;
  account: Account | null = null;
  selectedAmount: number = 1;
  display = 'none';

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private cartService: CartService,
    private produitService: ProduitService,
    private router: Router,
    private loginService: LoginService,
    private sousCategorieService: SousCategorieService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.activatedRoute.params.subscribe((params: Params) => {
      this.idProduit = params['id'];
      this.produitService.find(this.idProduit).subscribe({
        next: value => {
          this.sousCategorieService.find(value.body!.souscategorie!.id).subscribe({
            next: val => (value.body!.souscategorie!['categorie'] = val.body?.categorie),
          });
          this.course = value.body;
        },
      });
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  public counter(i: number): any[] {
    return new Array(i);
  }

  onSelected(value: string): void {
    this.selectedAmount = +value;
  }

  addToCart(course: IProduit, amount: number): void {
    if (this.account?.authorities.includes('ROLE_USER') && !this.account.authorities.includes('ROLE_ADMIN')) {
      this.cartService.addToCart(course, amount);
    } else {
      this.display = 'block';
    }
  }
  onCloseHandled(): void {
    this.display = 'none';
  }
  goToLogin(): void {
    if (this.account?.authorities.includes('ROLE_ADMIN')) {
      this.loginService.logout();
    }
    this.router.navigateByUrl('/login');
  }
}
