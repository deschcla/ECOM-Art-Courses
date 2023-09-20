import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CarteBancaireFormService, CarteBancaireFormGroup } from './carte-bancaire-form.service';
import { ICarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

@Component({
  selector: 'jhi-carte-bancaire-update',
  templateUrl: './carte-bancaire-update.component.html',
})
export class CarteBancaireUpdateComponent implements OnInit {
  isSaving = false;
  carteBancaire: ICarteBancaire | null = null;

  commandesSharedCollection: ICommande[] = [];

  editForm: CarteBancaireFormGroup = this.carteBancaireFormService.createCarteBancaireFormGroup();

  constructor(
    protected carteBancaireService: CarteBancaireService,
    protected carteBancaireFormService: CarteBancaireFormService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCommande = (o1: ICommande | null, o2: ICommande | null): boolean => this.commandeService.compareCommande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carteBancaire }) => {
      this.carteBancaire = carteBancaire;
      if (carteBancaire) {
        this.updateForm(carteBancaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carteBancaire = this.carteBancaireFormService.getCarteBancaire(this.editForm);
    if (carteBancaire.id !== null) {
      this.subscribeToSaveResponse(this.carteBancaireService.update(carteBancaire));
    } else {
      this.subscribeToSaveResponse(this.carteBancaireService.create(carteBancaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarteBancaire>>): void {
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

  protected updateForm(carteBancaire: ICarteBancaire): void {
    this.carteBancaire = carteBancaire;
    this.carteBancaireFormService.resetForm(this.editForm, carteBancaire);

    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing<ICommande>(
      this.commandesSharedCollection,
      carteBancaire.commande
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing<ICommande>(commandes, this.carteBancaire?.commande)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));
  }
}
