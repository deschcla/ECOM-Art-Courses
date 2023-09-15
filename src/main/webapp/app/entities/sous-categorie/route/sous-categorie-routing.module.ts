import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousCategorieComponent } from '../list/sous-categorie.component';
import { SousCategorieDetailComponent } from '../detail/sous-categorie-detail.component';
import { SousCategorieUpdateComponent } from '../update/sous-categorie-update.component';
import { SousCategorieRoutingResolveService } from './sous-categorie-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sousCategorieRoute: Routes = [
  {
    path: '',
    component: SousCategorieComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousCategorieDetailComponent,
    resolve: {
      sousCategorie: SousCategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousCategorieUpdateComponent,
    resolve: {
      sousCategorie: SousCategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousCategorieUpdateComponent,
    resolve: {
      sousCategorie: SousCategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousCategorieRoute)],
  exports: [RouterModule],
})
export class SousCategorieRoutingModule {}
