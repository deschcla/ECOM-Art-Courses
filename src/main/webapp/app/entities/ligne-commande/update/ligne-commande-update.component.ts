import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LigneCommandeFormService, LigneCommandeFormGroup } from './ligne-commande-form.service';
import { ILigneCommande } from '../ligne-commande.model';
import { LigneCommandeService } from '../service/ligne-commande.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

@Component({
  standalone: true,
  selector: 'jhi-ligne-commande-update',
  templateUrl: './ligne-commande-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LigneCommandeUpdateComponent implements OnInit {
  isSaving = false;
  ligneCommande: ILigneCommande | null = null;

  produitsSharedCollection: IProduit[] = [];
  commandesSharedCollection: ICommande[] = [];

  editForm: LigneCommandeFormGroup = this.ligneCommandeFormService.createLigneCommandeFormGroup();

  constructor(
    protected ligneCommandeService: LigneCommandeService,
    protected ligneCommandeFormService: LigneCommandeFormService,
    protected produitService: ProduitService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduit = (o1: IProduit | null, o2: IProduit | null): boolean => this.produitService.compareProduit(o1, o2);

  compareCommande = (o1: ICommande | null, o2: ICommande | null): boolean => this.commandeService.compareCommande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneCommande }) => {
      this.ligneCommande = ligneCommande;
      if (ligneCommande) {
        this.updateForm(ligneCommande);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligneCommande = this.ligneCommandeFormService.getLigneCommande(this.editForm);
    if (ligneCommande.id !== null) {
      this.subscribeToSaveResponse(this.ligneCommandeService.update(ligneCommande));
    } else {
      this.subscribeToSaveResponse(this.ligneCommandeService.create(ligneCommande));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigneCommande>>): void {
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

  protected updateForm(ligneCommande: ILigneCommande): void {
    this.ligneCommande = ligneCommande;
    this.ligneCommandeFormService.resetForm(this.editForm, ligneCommande);

    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing<IProduit>(
      this.produitsSharedCollection,
      ligneCommande.produit
    );
    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing<ICommande>(
      this.commandesSharedCollection,
      ligneCommande.commande
    );
  }

  protected loadRelationshipsOptions(): void {
    this.produitService
      .query()
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) => this.produitService.addProduitToCollectionIfMissing<IProduit>(produits, this.ligneCommande?.produit))
      )
      .subscribe((produits: IProduit[]) => (this.produitsSharedCollection = produits));

    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing<ICommande>(commandes, this.ligneCommande?.commande)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));
  }
}
