import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IReleveFacture } from '../releve-facture.model';
import { ReleveFactureService } from '../service/releve-facture.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './releve-facture-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReleveFactureDeleteDialogComponent {
  releveFacture?: IReleveFacture;

  constructor(protected releveFactureService: ReleveFactureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.releveFactureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
