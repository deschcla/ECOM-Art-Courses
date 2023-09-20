import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleveFacture } from '../releve-facture.model';
import { ReleveFactureService } from '../service/releve-facture.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './releve-facture-delete-dialog.component.html',
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
