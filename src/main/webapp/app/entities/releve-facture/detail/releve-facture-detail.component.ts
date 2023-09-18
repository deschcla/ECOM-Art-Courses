import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReleveFacture } from '../releve-facture.model';

@Component({
  selector: 'jhi-releve-facture-detail',
  templateUrl: './releve-facture-detail.component.html',
})
export class ReleveFactureDetailComponent implements OnInit {
  releveFacture: IReleveFacture | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releveFacture }) => {
      this.releveFacture = releveFacture;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
