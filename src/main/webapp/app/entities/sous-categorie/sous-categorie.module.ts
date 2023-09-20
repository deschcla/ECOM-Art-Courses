import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousCategorieComponent } from './list/sous-categorie.component';
import { SousCategorieDetailComponent } from './detail/sous-categorie-detail.component';
import { SousCategorieUpdateComponent } from './update/sous-categorie-update.component';
import { SousCategorieDeleteDialogComponent } from './delete/sous-categorie-delete-dialog.component';
import { SousCategorieRoutingModule } from './route/sous-categorie-routing.module';

@NgModule({
  imports: [SharedModule, SousCategorieRoutingModule],
  declarations: [SousCategorieComponent, SousCategorieDetailComponent, SousCategorieUpdateComponent, SousCategorieDeleteDialogComponent],
})
export class SousCategorieModule {}
