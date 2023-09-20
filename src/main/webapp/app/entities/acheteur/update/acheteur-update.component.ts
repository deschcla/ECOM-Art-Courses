import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AcheteurFormService, AcheteurFormGroup } from './acheteur-form.service';
import { IAcheteur } from '../acheteur.model';
import { AcheteurService } from '../service/acheteur.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-acheteur-update',
  templateUrl: './acheteur-update.component.html',
})
export class AcheteurUpdateComponent implements OnInit {
  isSaving = false;
  acheteur: IAcheteur | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: AcheteurFormGroup = this.acheteurFormService.createAcheteurFormGroup();

  constructor(
    protected acheteurService: AcheteurService,
    protected acheteurFormService: AcheteurFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acheteur }) => {
      this.acheteur = acheteur;
      if (acheteur) {
        this.updateForm(acheteur);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const acheteur = this.acheteurFormService.getAcheteur(this.editForm);
    if (acheteur.id !== null) {
      this.subscribeToSaveResponse(this.acheteurService.update(acheteur));
    } else {
      this.subscribeToSaveResponse(this.acheteurService.create(acheteur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAcheteur>>): void {
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

  protected updateForm(acheteur: IAcheteur): void {
    this.acheteur = acheteur;
    this.acheteurFormService.resetForm(this.editForm, acheteur);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, acheteur.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.acheteur?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
