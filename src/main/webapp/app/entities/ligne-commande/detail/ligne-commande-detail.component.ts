import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILigneCommande } from '../ligne-commande.model';

@Component({
  standalone: true,
  selector: 'jhi-ligne-commande-detail',
  templateUrl: './ligne-commande-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LigneCommandeDetailComponent {
  @Input() ligneCommande: ILigneCommande | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
