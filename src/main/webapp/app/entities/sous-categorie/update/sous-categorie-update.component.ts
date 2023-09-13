import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SousCategorieFormService, SousCategorieFormGroup } from './sous-categorie-form.service';
import { ISousCategorie } from '../sous-categorie.model';
import { SousCategorieService } from '../service/sous-categorie.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

@Component({
  standalone: true,
  selector: 'jhi-sous-categorie-update',
  templateUrl: './sous-categorie-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SousCategorieUpdateComponent implements OnInit {
  isSaving = false;
  sousCategorie: ISousCategorie | null = null;

  categoriesSharedCollection: ICategorie[] = [];

  editForm: SousCategorieFormGroup = this.sousCategorieFormService.createSousCategorieFormGroup();

  constructor(
    protected sousCategorieService: SousCategorieService,
    protected sousCategorieFormService: SousCategorieFormService,
    protected categorieService: CategorieService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategorie = (o1: ICategorie | null, o2: ICategorie | null): boolean => this.categorieService.compareCategorie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousCategorie }) => {
      this.sousCategorie = sousCategorie;
      if (sousCategorie) {
        this.updateForm(sousCategorie);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousCategorie = this.sousCategorieFormService.getSousCategorie(this.editForm);
    if (sousCategorie.id !== null) {
      this.subscribeToSaveResponse(this.sousCategorieService.update(sousCategorie));
    } else {
      this.subscribeToSaveResponse(this.sousCategorieService.create(sousCategorie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousCategorie>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sousCategorie: ISousCategorie): void {
    this.sousCategorie = sousCategorie;
    this.sousCategorieFormService.resetForm(this.editForm, sousCategorie);

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(
      this.categoriesSharedCollection,
      sousCategorie.categorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(categories, this.sousCategorie?.categorie)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));
  }
}
