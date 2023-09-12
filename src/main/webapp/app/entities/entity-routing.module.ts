import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'acheteur',
        data: { pageTitle: 'ecomArtCoursesApp.acheteur.home.title' },
        loadChildren: () => import('./acheteur/acheteur.routes'),
      },
      {
        path: 'produit',
        data: { pageTitle: 'ecomArtCoursesApp.produit.home.title' },
        loadChildren: () => import('./produit/produit.routes'),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'ecomArtCoursesApp.categorie.home.title' },
        loadChildren: () => import('./categorie/categorie.routes'),
      },
      {
        path: 'sous-categorie',
        data: { pageTitle: 'ecomArtCoursesApp.sousCategorie.home.title' },
        loadChildren: () => import('./sous-categorie/sous-categorie.routes'),
      },
      {
        path: 'commande',
        data: { pageTitle: 'ecomArtCoursesApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.routes'),
      },
      {
        path: 'ligne-commande',
        data: { pageTitle: 'ecomArtCoursesApp.ligneCommande.home.title' },
        loadChildren: () => import('./ligne-commande/ligne-commande.routes'),
      },
      {
        path: 'carte-bancaire',
        data: { pageTitle: 'ecomArtCoursesApp.carteBancaire.home.title' },
        loadChildren: () => import('./carte-bancaire/carte-bancaire.routes'),
      },
      {
        path: 'releve-facture',
        data: { pageTitle: 'ecomArtCoursesApp.releveFacture.home.title' },
        loadChildren: () => import('./releve-facture/releve-facture.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
