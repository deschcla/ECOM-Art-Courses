import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousCategorie } from '../sous-categorie.model';
import { SousCategorieService } from '../service/sous-categorie.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './sous-categorie-delete-dialog.component.html',
})
export class SousCategorieDeleteDialogComponent {
  sousCategorie?: ISousCategorie;

  constructor(protected sousCategorieService: SousCategorieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousCategorieService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
