import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { CartService } from 'app/core/util/cart.service';
import { Subject, takeUntil } from 'rxjs';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { LoginService } from '../login/login.service';
import { SousCategorieService } from 'app/entities/sous-categorie/service/sous-categorie.service';

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
    private produitService: ProduitService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    if (this.courses?.length === 0) {
      this.produitService.query().subscribe({
        next: value => {
          this.cartService.fillCourses(value.body!);
        },
        error: error => console.log(error),
      });
    }
    this.cartService.courseChange.subscribe(value => {
      this.courses = value;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  viewDetails(course: IProduit): void {
    this.router.navigateByUrl('/course-details/' + course.id.toString());
  }

  addToCart(course: IProduit, event: Event): void {
    console.log(event);

    if (this.account?.authorities.includes('ROLE_USER') && !this.account.authorities.includes('ROLE_ADMIN')) {
      this.cartService.addToCart(course, 1);
      course.clicked = true;
    } else {
      this.display = 'block';
    }
    event.stopPropagation();
  }
  onCloseHandled(event: Event): void {
    this.display = 'none';
    event.stopPropagation();
  }
  goToLogin(event: Event): void {
    if (this.account?.authorities.includes('ROLE_ADMIN')) {
      this.loginService.logout();
    }
    this.router.navigateByUrl('/login');
    event.stopPropagation();
  }
}
