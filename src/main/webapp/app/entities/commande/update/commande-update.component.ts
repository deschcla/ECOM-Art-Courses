import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CommandeFormService, CommandeFormGroup } from './commande-form.service';
import { ICommande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { IReleveFacture } from 'app/entities/releve-facture/releve-facture.model';
import { ReleveFactureService } from 'app/entities/releve-facture/service/releve-facture.service';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';
import { AcheteurService } from 'app/entities/acheteur/service/acheteur.service';

@Component({
  standalone: true,
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;
  commande: ICommande | null = null;

  produitsSharedCollection: IProduit[] = [];
  releveFacturesSharedCollection: IReleveFacture[] = [];
  acheteursSharedCollection: IAcheteur[] = [];

  editForm: CommandeFormGroup = this.commandeFormService.createCommandeFormGroup();

  constructor(
    protected commandeService: CommandeService,
    protected commandeFormService: CommandeFormService,
    protected produitService: ProduitService,
    protected releveFactureService: ReleveFactureService,
    protected acheteurService: AcheteurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduit = (o1: IProduit | null, o2: IProduit | null): boolean => this.produitService.compareProduit(o1, o2);

  compareReleveFacture = (o1: IReleveFacture | null, o2: IReleveFacture | null): boolean =>
    this.releveFactureService.compareReleveFacture(o1, o2);

  compareAcheteur = (o1: IAcheteur | null, o2: IAcheteur | null): boolean => this.acheteurService.compareAcheteur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      this.commande = commande;
      if (commande) {
        this.updateForm(commande);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commande = this.commandeFormService.getCommande(this.editForm);
    if (commande.id !== null) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
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

  protected updateForm(commande: ICommande): void {
    this.commande = commande;
    this.commandeFormService.resetForm(this.editForm, commande);

    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing<IProduit>(
      this.produitsSharedCollection,
      ...(commande.produits ?? [])
    );
    this.releveFacturesSharedCollection = this.releveFactureService.addReleveFactureToCollectionIfMissing<IReleveFacture>(
      this.releveFacturesSharedCollection,
      commande.releveFacture
    );
    this.acheteursSharedCollection = this.acheteurService.addAcheteurToCollectionIfMissing<IAcheteur>(
      this.acheteursSharedCollection,
      commande.acheteur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.produitService
      .query()
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) =>
          this.produitService.addProduitToCollectionIfMissing<IProduit>(produits, ...(this.commande?.produits ?? []))
        )
      )
      .subscribe((produits: IProduit[]) => (this.produitsSharedCollection = produits));

    this.releveFactureService
      .query()
      .pipe(map((res: HttpResponse<IReleveFacture[]>) => res.body ?? []))
      .pipe(
        map((releveFactures: IReleveFacture[]) =>
          this.releveFactureService.addReleveFactureToCollectionIfMissing<IReleveFacture>(releveFactures, this.commande?.releveFacture)
        )
      )
      .subscribe((releveFactures: IReleveFacture[]) => (this.releveFacturesSharedCollection = releveFactures));

    this.acheteurService
      .query()
      .pipe(map((res: HttpResponse<IAcheteur[]>) => res.body ?? []))
      .pipe(
        map((acheteurs: IAcheteur[]) =>
          this.acheteurService.addAcheteurToCollectionIfMissing<IAcheteur>(acheteurs, this.commande?.acheteur)
        )
      )
      .subscribe((acheteurs: IAcheteur[]) => (this.acheteursSharedCollection = acheteurs));
  }
}
