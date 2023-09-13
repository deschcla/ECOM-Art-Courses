import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICarteBancaire } from '../carte-bancaire.model';

@Component({
  standalone: true,
  selector: 'jhi-carte-bancaire-detail',
  templateUrl: './carte-bancaire-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CarteBancaireDetailComponent {
  @Input() carteBancaire: ICarteBancaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
