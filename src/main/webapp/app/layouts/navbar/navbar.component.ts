import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { StateStorageService } from 'app/core/auth/state-storage.service';
import { SharedModule } from 'app/shared/shared.module';
import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { ActiveMenuDirective } from './active-menu.directive';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import NavbarItem from './navbar-item.model';
import { CartService } from 'app/core/util/cart.service';
import { FormGroup, FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IProduit } from '../../entities/produit/produit.model';
import { ProduitService } from '../../entities/produit/service/produit.service';
import { NotificationService } from 'app/core/util/notification.service';

@Component({
  standalone: true,
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  imports: [RouterModule, SharedModule, FormsModule, ReactiveFormsModule, ActiveMenuDirective],
})
export default class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: NavbarItem[] = [];
  counter: number = 0;
  courses: IProduit[] | null = [];

  searchForm = new FormGroup({
    search: new FormControl(''),
  });

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private stateStorageService: StateStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private cartService: CartService,
    private produitService: ProduitService,
    private ntfService: NotificationService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    this.cartService.counterChange.subscribe(value => {
      this.counter = value;
    });
    this.cartService.searchChange.subscribe(value => {
      this.searchForm.controls.search.setValue(value);
    });

    this.produitService.query().subscribe({
      next: value => (this.courses = value.body),
      error: error => this.ntfService.notifyBanner('Error', error),
      complete: () => {
        this.router.events.subscribe(() => {
          if (this.searchForm.value.search !== '') {
            this.onKeyUp();
          }
        });
      },
    });
  }

  changeLanguage(languageKey: string): void {
    this.stateStorageService.storeUrl(languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  search(): void {
    this.router.navigate(['']);
  }

  onKeyUp(): void {
    const res = this.courses?.filter(course => course.nomProduit?.toLowerCase()?.includes(this.searchForm.value.search!.toLowerCase()));
    this.cartService.fillCourses(res!);
  }
}
