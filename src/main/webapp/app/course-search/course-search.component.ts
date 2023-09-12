import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { CartService } from 'app/core/util/cart.service';
import { Subject, takeUntil } from 'rxjs';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IProduit } from 'app/entities/produit/produit.model';

@Component({
  selector: 'jhi-course-search',
  templateUrl: './course-search.component.html',
  styleUrls: ['./course-search.component.scss'],
})
export class CourseSearchComponent implements OnInit, OnDestroy {
  courses: IProduit[] | null = [];
  account: Account | null = null;
  display = 'none';
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private cartService: CartService,
    private produitService: ProduitService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.produitService.query().subscribe({
      next: value => (this.courses = value.body),
      error: error => console.log(error),
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  viewDetails(course: IProduit): void {
    this.router.navigateByUrl('/course-details/' + course.id.toString(), { state: course });
  }

  addToCart(course: IProduit, event: Event): void {
    if (this.account?.activated) {
      // this.notifierService.notify('success', 'You are awesome! I mean it!');
      this.cartService.addToCart(course, 1);
      console.log(this.cartService.cart);
    } else {
      this.display = 'block';

      // this.signedOut = false;
      // document.getElementById('exampleModalCenter').modal(options)
    }
    event.stopPropagation();
  }
  onCloseHandled(event: Event): void {
    this.display = 'none';
    event.stopPropagation();
  }
  goToLogin(event: Event): void {
    this.router.navigateByUrl('/login');
    event.stopPropagation();
  }
}
