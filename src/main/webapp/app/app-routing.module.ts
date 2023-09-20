import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseDetailsComponent } from './course-details/course-details.component';
import { CourseSearchComponent } from './course-search/course-search.component';
import { PaymentComponent } from './payment/payment.component';
import { CartComponent } from './cart/cart.component';
import { AuthGuardService } from './entities/auth-guards/auth-guard.service';
import { FactureComponent } from './facture/facture.component';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: '',
          component: CourseSearchComponent,
          title: 'Home',
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },
        {
          path: 'course-details/:id',
          component: CourseDetailsComponent,
        },
        {
          path: 'cart',
          component: CartComponent,
          title: 'cart.title',
          canActivate: [AuthGuardService],
        },
        {
          path: 'payment',
          component: PaymentComponent,
          title: 'payment.title',
          canActivate: [AuthGuardService],
        },
        {
          path: 'facture',
          component: FactureComponent,
          title: 'facture.title',
          canActivate: [AuthGuardService],
        },
        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(m => m.EntityRoutingModule),
        },
        navbarRoute,
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
