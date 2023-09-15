import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousCategorie } from '../sous-categorie.model';

@Component({
  selector: 'jhi-sous-categorie-detail',
  templateUrl: './sous-categorie-detail.component.html',
})
export class SousCategorieDetailComponent implements OnInit {
  sousCategorie: ISousCategorie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousCategorie }) => {
      this.sousCategorie = sousCategorie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
