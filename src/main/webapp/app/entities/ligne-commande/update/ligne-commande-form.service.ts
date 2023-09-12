import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILigneCommande, NewLigneCommande } from '../ligne-commande.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILigneCommande for edit and NewLigneCommandeFormGroupInput for create.
 */
type LigneCommandeFormGroupInput = ILigneCommande | PartialWithRequiredKeyOf<NewLigneCommande>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILigneCommande | NewLigneCommande> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

type LigneCommandeFormRawValue = FormValueOf<ILigneCommande>;

type NewLigneCommandeFormRawValue = FormValueOf<NewLigneCommande>;

type LigneCommandeFormDefaults = Pick<NewLigneCommande, 'id' | 'createdAt' | 'updateAt'>;

type LigneCommandeFormGroupContent = {
  id: FormControl<LigneCommandeFormRawValue['id'] | NewLigneCommande['id']>;
  quantite: FormControl<LigneCommandeFormRawValue['quantite']>;
  montant: FormControl<LigneCommandeFormRawValue['montant']>;
  valided: FormControl<LigneCommandeFormRawValue['valided']>;
  createdAt: FormControl<LigneCommandeFormRawValue['createdAt']>;
  updateAt: FormControl<LigneCommandeFormRawValue['updateAt']>;
  produit: FormControl<LigneCommandeFormRawValue['produit']>;
  commande: FormControl<LigneCommandeFormRawValue['commande']>;
};

export type LigneCommandeFormGroup = FormGroup<LigneCommandeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LigneCommandeFormService {
  createLigneCommandeFormGroup(ligneCommande: LigneCommandeFormGroupInput = { id: null }): LigneCommandeFormGroup {
    const ligneCommandeRawValue = this.convertLigneCommandeToLigneCommandeRawValue({
      ...this.getFormDefaults(),
      ...ligneCommande,
    });
    return new FormGroup<LigneCommandeFormGroupContent>({
      id: new FormControl(
        { value: ligneCommandeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      quantite: new FormControl(ligneCommandeRawValue.quantite),
      montant: new FormControl(ligneCommandeRawValue.montant),
      valided: new FormControl(ligneCommandeRawValue.valided),
      createdAt: new FormControl(ligneCommandeRawValue.createdAt),
      updateAt: new FormControl(ligneCommandeRawValue.updateAt),
      produit: new FormControl(ligneCommandeRawValue.produit, {
        validators: [Validators.required],
      }),
      commande: new FormControl(ligneCommandeRawValue.commande, {
        validators: [Validators.required],
      }),
    });
  }

  getLigneCommande(form: LigneCommandeFormGroup): ILigneCommande | NewLigneCommande {
    return this.convertLigneCommandeRawValueToLigneCommande(form.getRawValue() as LigneCommandeFormRawValue | NewLigneCommandeFormRawValue);
  }

  resetForm(form: LigneCommandeFormGroup, ligneCommande: LigneCommandeFormGroupInput): void {
    const ligneCommandeRawValue = this.convertLigneCommandeToLigneCommandeRawValue({ ...this.getFormDefaults(), ...ligneCommande });
    form.reset(
      {
        ...ligneCommandeRawValue,
        id: { value: ligneCommandeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LigneCommandeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updateAt: currentTime,
    };
  }

  private convertLigneCommandeRawValueToLigneCommande(
    rawLigneCommande: LigneCommandeFormRawValue | NewLigneCommandeFormRawValue
  ): ILigneCommande | NewLigneCommande {
    return {
      ...rawLigneCommande,
      createdAt: dayjs(rawLigneCommande.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawLigneCommande.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertLigneCommandeToLigneCommandeRawValue(
    ligneCommande: ILigneCommande | (Partial<NewLigneCommande> & LigneCommandeFormDefaults)
  ): LigneCommandeFormRawValue | PartialWithRequiredKeyOf<NewLigneCommandeFormRawValue> {
    return {
      ...ligneCommande,
      createdAt: ligneCommande.createdAt ? ligneCommande.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: ligneCommande.updateAt ? ligneCommande.updateAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
