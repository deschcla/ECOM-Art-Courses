import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommandeComponent } from './list/commande.component';
import { CommandeDetailComponent } from './detail/commande-detail.component';
import { CommandeUpdateComponent } from './update/commande-update.component';
import CommandeResolve from './route/commande-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const commandeRoute: Routes = [
  {
    path: '',
    component: CommandeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommandeDetailComponent,
    resolve: {
      commande: CommandeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommandeUpdateComponent,
    resolve: {
      commande: CommandeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommandeUpdateComponent,
    resolve: {
      commande: CommandeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default commandeRoute;
