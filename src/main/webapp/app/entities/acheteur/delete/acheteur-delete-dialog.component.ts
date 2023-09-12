import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IAcheteur } from '../acheteur.model';
import { AcheteurService } from '../service/acheteur.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './acheteur-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AcheteurDeleteDialogComponent {
  acheteur?: IAcheteur;

  constructor(protected acheteurService: AcheteurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acheteurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
