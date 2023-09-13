import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReleveFactureComponent } from './list/releve-facture.component';
import { ReleveFactureDetailComponent } from './detail/releve-facture-detail.component';
import { ReleveFactureUpdateComponent } from './update/releve-facture-update.component';
import ReleveFactureResolve from './route/releve-facture-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const releveFactureRoute: Routes = [
  {
    path: '',
    component: ReleveFactureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReleveFactureDetailComponent,
    resolve: {
      releveFacture: ReleveFactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReleveFactureUpdateComponent,
    resolve: {
      releveFacture: ReleveFactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReleveFactureUpdateComponent,
    resolve: {
      releveFacture: ReleveFactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default releveFactureRoute;
