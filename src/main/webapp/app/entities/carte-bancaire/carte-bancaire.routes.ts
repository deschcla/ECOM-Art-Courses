import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CarteBancaireComponent } from './list/carte-bancaire.component';
import { CarteBancaireDetailComponent } from './detail/carte-bancaire-detail.component';
import { CarteBancaireUpdateComponent } from './update/carte-bancaire-update.component';
import CarteBancaireResolve from './route/carte-bancaire-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const carteBancaireRoute: Routes = [
  {
    path: '',
    component: CarteBancaireComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CarteBancaireDetailComponent,
    resolve: {
      carteBancaire: CarteBancaireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CarteBancaireUpdateComponent,
    resolve: {
      carteBancaire: CarteBancaireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CarteBancaireUpdateComponent,
    resolve: {
      carteBancaire: CarteBancaireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default carteBancaireRoute;
