import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReleveFactureComponent } from '../list/releve-facture.component';
import { ReleveFactureDetailComponent } from '../detail/releve-facture-detail.component';
import { ReleveFactureUpdateComponent } from '../update/releve-facture-update.component';
import { ReleveFactureRoutingResolveService } from './releve-facture-routing-resolve.service';
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
      releveFacture: ReleveFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReleveFactureUpdateComponent,
    resolve: {
      releveFacture: ReleveFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReleveFactureUpdateComponent,
    resolve: {
      releveFacture: ReleveFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(releveFactureRoute)],
  exports: [RouterModule],
})
export class ReleveFactureRoutingModule {}
