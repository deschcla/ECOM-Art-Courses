import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ReleveFactureFormService, ReleveFactureFormGroup } from './releve-facture-form.service';
import { IReleveFacture } from '../releve-facture.model';
import { ReleveFactureService } from '../service/releve-facture.service';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';
import { AcheteurService } from 'app/entities/acheteur/service/acheteur.service';

@Component({
  selector: 'jhi-releve-facture-update',
  templateUrl: './releve-facture-update.component.html',
})
export class ReleveFactureUpdateComponent implements OnInit {
  isSaving = false;
  releveFacture: IReleveFacture | null = null;

  acheteursSharedCollection: IAcheteur[] = [];

  editForm: ReleveFactureFormGroup = this.releveFactureFormService.createReleveFactureFormGroup();

  constructor(
    protected releveFactureService: ReleveFactureService,
    protected releveFactureFormService: ReleveFactureFormService,
    protected acheteurService: AcheteurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAcheteur = (o1: IAcheteur | null, o2: IAcheteur | null): boolean => this.acheteurService.compareAcheteur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releveFacture }) => {
      this.releveFacture = releveFacture;
      if (releveFacture) {
        this.updateForm(releveFacture);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const releveFacture = this.releveFactureFormService.getReleveFacture(this.editForm);
    if (releveFacture.id !== null) {
      this.subscribeToSaveResponse(this.releveFactureService.update(releveFacture));
    } else {
      this.subscribeToSaveResponse(this.releveFactureService.create(releveFacture));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReleveFacture>>): void {
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

  protected updateForm(releveFacture: IReleveFacture): void {
    this.releveFacture = releveFacture;
    this.releveFactureFormService.resetForm(this.editForm, releveFacture);

    this.acheteursSharedCollection = this.acheteurService.addAcheteurToCollectionIfMissing<IAcheteur>(
      this.acheteursSharedCollection,
      releveFacture.acheteur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.acheteurService
      .query()
      .pipe(map((res: HttpResponse<IAcheteur[]>) => res.body ?? []))
      .pipe(
        map((acheteurs: IAcheteur[]) =>
          this.acheteurService.addAcheteurToCollectionIfMissing<IAcheteur>(acheteurs, this.releveFacture?.acheteur)
        )
      )
      .subscribe((acheteurs: IAcheteur[]) => (this.acheteursSharedCollection = acheteurs));
  }
}
