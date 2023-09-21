import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      // {
      //   path: 'acheteur',
      //   data: { pageTitle: 'ecomArtCoursesApp.acheteur.home.title' },
      //   loadChildren: () => import('./acheteur/acheteur.module').then(m => m.AcheteurModule),
      // },
      {
        path: 'produit',
        data: { pageTitle: 'ecomArtCoursesApp.produit.home.title' },
        loadChildren: () => import('./produit/produit.module').then(m => m.ProduitModule),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'ecomArtCoursesApp.categorie.home.title' },
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CategorieModule),
      },
      {
        path: 'sous-categorie',
        data: { pageTitle: 'ecomArtCoursesApp.sousCategorie.home.title' },
        loadChildren: () => import('./sous-categorie/sous-categorie.module').then(m => m.SousCategorieModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'ecomArtCoursesApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'ligne-commande',
        data: { pageTitle: 'ecomArtCoursesApp.ligneCommande.home.title' },
        loadChildren: () => import('./ligne-commande/ligne-commande.module').then(m => m.LigneCommandeModule),
      },
      {
        path: 'carte-bancaire',
        data: { pageTitle: 'ecomArtCoursesApp.carteBancaire.home.title' },
        loadChildren: () => import('./carte-bancaire/carte-bancaire.module').then(m => m.CarteBancaireModule),
      },
      {
        path: 'releve-facture',
        data: { pageTitle: 'ecomArtCoursesApp.releveFacture.home.title' },
        loadChildren: () => import('./releve-facture/releve-facture.module').then(m => m.ReleveFactureModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
