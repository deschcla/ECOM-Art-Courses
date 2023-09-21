import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { CartService } from 'app/core/util/cart.service';
import { Subject, takeUntil } from 'rxjs';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { LoginService } from '../login/login.service';
import { S3Service } from '../S3/s3.service';
import { NotificationService } from '../core/util/notification.service';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-course-search',
  templateUrl: './course-search.component.html',
  styleUrls: ['./course-search.component.scss'],
})
export class CourseSearchComponent implements OnInit, OnDestroy {
  courses: IProduit[] | null = [];
  coursesUpdated: IProduit[] | null = [];
  account: Account | null = null;
  display = 'none';
  private readonly destroy$ = new Subject<void>();
  key?: string;
  constructor(
    private accountService: AccountService,
    private router: Router,
    private cartService: CartService,
    private produitService: ProduitService,
    private loginService: LoginService,
    private s3Service: S3Service,
    private ntfService: NotificationService,
    private titleService: Title,
    private translateService: TranslateService
  ) {}
  ngOnInit(): void {
    this.translateService.get('course-search.title').subscribe(title => this.titleService.setTitle(title));
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.getProducts();
    this.cartService.courseChange.subscribe({
      next: value => (this.courses = value),
      complete: () => this.getProducts(),
    });
  }
  async updateCourses(courses) {
    this.getURL(courses);
  }

  getProducts(): void {
    if (this.courses?.length === 0) {
      this.produitService.query().subscribe({
        next: value => {
          this.updateCourses(value.body!);
          this.cartService.fillCourses(value.body!);
        },
        error: error => console.log(error),
      });
    }
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  viewDetails(course: IProduit): void {
    this.router.navigateByUrl('/course-details/' + course.id.toString());
  }
  async getURL(objects: IProduit[]) {
    for (const obj of objects) {
      (await this.s3Service.getImageFromS3(obj.lienImg!)).subscribe(res => {
        obj.lienImg = res.body;
        this.coursesUpdated?.push({
          ...obj,
          lienImg: obj.lienImg || '',
        });
      });
    }
  }

  async getImageURL(key: string) {
    (await this.s3Service.getImageFromS3(key)).subscribe(
      response => {
        this.key = response.key;
      },
      error => {
        console.error('Error:', error);
      }
    );
  }
  addToCart(course: IProduit, event: Event): void {
    if (this.account?.authorities.includes('ROLE_USER') && !this.account.authorities.includes('ROLE_ADMIN')) {
      this.cartService.addToCart(course, 1);
      course.clicked = true;
    } else {
      this.display = 'block';
      this.ntfService.notifyBanner('Error', "Échec de l'ajout au panier, veuillez réssayer");
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
