import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import HomeComponent from './home/home.component';
import NavbarComponent from './layouts/navbar/navbar.component';
import LoginComponent from './login/login.component';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourseDetailsComponent } from './course-details/course-details.component';
import { CourseSearchComponent } from './course-search/course-search.component';
import { PaymentComponent } from './payment/payment.component';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'home',
          component: HomeComponent,
          title: 'home.title',
        },
        {
          path: '',
          component: CourseSearchComponent,
          title: 'course-search.title',
        },
        {
          path: '',
          component: NavbarComponent,
          outlet: 'navbar',
        },
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module'),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.route'),
        },
        {
          path: 'login',
          component: LoginComponent,
          title: 'login.title',
        },
        {
          path: 'course-details/:id',
          component: CourseDetailsComponent,
          title: 'course-details.title',
        },
        {
          path: 'payment',
          component: PaymentComponent,
          title: 'payment.title',
        },
        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(({ EntityRoutingModule }) => EntityRoutingModule),
        },
        // { path: '**', redirectTo: 'course-search' },
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED, bindToComponentInputs: true }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
