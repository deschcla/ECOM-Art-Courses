import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ISousCategorie } from '../sous-categorie.model';
import { SousCategorieService } from '../service/sous-categorie.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './sous-categorie-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
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
