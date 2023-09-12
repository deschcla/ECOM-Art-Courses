import { Component, OnDestroy, OnInit } from '@angular/core';
import { Course } from '../core/request/course.model';
import { ActivatedRoute, Params } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { Subject, takeUntil } from 'rxjs';
import { CartService } from 'app/core/util/cart.service';

@Component({
  selector: 'jhi-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.scss'],
})
export class CourseDetailsComponent implements OnInit, OnDestroy {
  idProduit: number;
  course: Course;
  account: Account | null = null;
  selectedAmount: number = 1;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private activatedRoute: ActivatedRoute, private cartService: CartService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.activatedRoute.params.subscribe((params: Params) => {
      this.idProduit = params['id'];
      this.course = window.history.state;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  public counter(i: number): any[] {
    return new Array(i);
  }

  addToCart(course: Course, amount: number): void {
    if (this.account?.activated) {
      this.cartService.addToCart(course, amount);
    }
  }

  onSelected(value: string): void {
    this.selectedAmount = +value;
  }
}
