import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AcheteurComponent } from './list/acheteur.component';
import { AcheteurDetailComponent } from './detail/acheteur-detail.component';
import { AcheteurUpdateComponent } from './update/acheteur-update.component';
import AcheteurResolve from './route/acheteur-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const acheteurRoute: Routes = [
  {
    path: '',
    component: AcheteurComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AcheteurDetailComponent,
    resolve: {
      acheteur: AcheteurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AcheteurUpdateComponent,
    resolve: {
      acheteur: AcheteurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AcheteurUpdateComponent,
    resolve: {
      acheteur: AcheteurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default acheteurRoute;
