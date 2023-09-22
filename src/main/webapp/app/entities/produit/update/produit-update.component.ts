import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProduitFormService, ProduitFormGroup } from './produit-form.service';
import { IProduit } from '../produit.model';
import { ProduitService } from '../service/produit.service';
import { ISousCategorie } from 'app/entities/sous-categorie/sous-categorie.model';
import { SousCategorieService } from 'app/entities/sous-categorie/service/sous-categorie.service';
import { S3Service } from '../../../S3/s3.service';
@Component({
  selector: 'jhi-produit-update',
  templateUrl: './produit-update.component.html',
})
export class ProduitUpdateComponent implements OnInit {
  isSaving = false;
  produit: IProduit | null = null;

  sousCategoriesSharedCollection: ISousCategorie[] = [];

  editForm: ProduitFormGroup = this.produitFormService.createProduitFormGroup();
  selectedFileBase64: string = '';
  imageKey: string = '';

  constructor(
    protected produitService: ProduitService,
    protected produitFormService: ProduitFormService,
    protected sousCategorieService: SousCategorieService,
    protected activatedRoute: ActivatedRoute,
    protected s3Service: S3Service
  ) {}

  compareSousCategorie = (o1: ISousCategorie | null, o2: ISousCategorie | null): boolean =>
    this.sousCategorieService.compareSousCategorie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produit }) => {
      this.produit = produit;
      if (produit) {
        this.updateForm(produit);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produit = this.produitFormService.getProduit(this.editForm);
    if (produit.id !== null) {
      this.subscribeToSaveResponse(this.produitService.update(produit));
    } else {
      this.subscribeToSaveResponse(this.produitService.create(produit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduit>>): void {
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

  protected updateForm(produit: IProduit): void {
    this.produit = produit;
    this.produitFormService.resetForm(this.editForm, produit);

    this.sousCategoriesSharedCollection = this.sousCategorieService.addSousCategorieToCollectionIfMissing<ISousCategorie>(
      this.sousCategoriesSharedCollection,
      produit.souscategorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sousCategorieService
      .query()
      .pipe(map((res: HttpResponse<ISousCategorie[]>) => res.body ?? []))
      .pipe(
        map((sousCategories: ISousCategorie[]) =>
          this.sousCategorieService.addSousCategorieToCollectionIfMissing<ISousCategorie>(sousCategories, this.produit?.souscategorie)
        )
      )
      .subscribe((sousCategories: ISousCategorie[]) => (this.sousCategoriesSharedCollection = sousCategories));
  }

  async uploadImage(event) {
    if (!event) return;
    let file = event.target.files[0];
    if (file) {
      (await this.s3Service.uploadToS3(file, this.selectedFileBase64!)).subscribe(
        response => {
          this.imageKey = response.key;
        },
        error => {
          console.error('Error:', error);
        }
      );
    } else {
      console.log('No image selected.');
    }
  }
}
