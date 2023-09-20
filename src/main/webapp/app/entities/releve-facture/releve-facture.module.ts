import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReleveFactureComponent } from './list/releve-facture.component';
import { ReleveFactureDetailComponent } from './detail/releve-facture-detail.component';
import { ReleveFactureUpdateComponent } from './update/releve-facture-update.component';
import { ReleveFactureDeleteDialogComponent } from './delete/releve-facture-delete-dialog.component';
import { ReleveFactureRoutingModule } from './route/releve-facture-routing.module';

@NgModule({
  imports: [SharedModule, ReleveFactureRoutingModule],
  declarations: [ReleveFactureComponent, ReleveFactureDetailComponent, ReleveFactureUpdateComponent, ReleveFactureDeleteDialogComponent],
})
export class ReleveFactureModule {}
