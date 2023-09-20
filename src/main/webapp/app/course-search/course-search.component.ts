import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { CartService } from 'app/core/util/cart.service';
import { Subject, takeUntil } from 'rxjs';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { LoginService } from '../login/login.service';
import { HttpClient } from '@angular/common/http';
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
  selectedFile?: string;
  constructor(
    private accountService: AccountService,
    private router: Router,
    private cartService: CartService,
    private produitService: ProduitService,
    private loginService: LoginService,
    private http: HttpClient
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
    this.router.navigateByUrl('/course-details/' + course.id.toString());
  }
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    const formData = new FormData();

    formData.append('file', this.selectedFile!);
    console.log(this.selectedFile);

    this.http.post('/api/produits/upload', formData).subscribe(
      response => {
        alert('Image uploaded successfully');
      },
      error => {
        console.error('Image upload failed', error);
      }
    );
  }
  /**
   * uploader une image en local
   * @param event
   * @return {Promise<void>}
   */
  async uploadImage(event) {
    if (!event) return;
    let file = event.target.files[0];
    event.target.value = '';
    await this.encodeImageToBase64(file).then(res => {
      this.selectedFile = res.toString();
    });
    const formData = new FormData();
    formData.append('file', this.selectedFile!);

    this.http.post('api/produits/upload', formData).subscribe(
      response => {
        alert('Image uploaded successfully');
      },
      error => {
        console.error('Image upload failed', error);
      }
    );
  }
  encodeImageToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = event => {
        if (event.target) {
          const base64String = event.target.result as string;
          resolve(base64String);
        } else {
          reject(new Error('Error reading file.'));
        }
      };

      reader.readAsDataURL(file);
    });
  }

  // async toBase64(file) {
  //     return new Promise<string>((resolve, reject) => {
  //       const reader = new FileReader();
  //       reader.readAsDataURL(file);
  //       reader.onload = () => resolve(reader.result);
  //       reader.onerror = error => reject(error);
  //   })
  //
  // }

  addToCart(course: IProduit, event: Event): void {
    if (this.account?.authorities.includes('ROLE_USER') && !this.account.authorities.includes('ROLE_ADMIN')) {
      this.cartService.addToCart(course, 1);
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
