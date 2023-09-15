import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduit, NewProduit } from '../produit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduit for edit and NewProduitFormGroupInput for create.
 */
type ProduitFormGroupInput = IProduit | PartialWithRequiredKeyOf<NewProduit>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduit | NewProduit> = Omit<T, 'date' | 'createdAt' | 'updateAt'> & {
  date?: string | null;
  createdAt?: string | null;
  updateAt?: string | null;
};

type ProduitFormRawValue = FormValueOf<IProduit>;

type NewProduitFormRawValue = FormValueOf<NewProduit>;

type ProduitFormDefaults = Pick<NewProduit, 'id' | 'date' | 'createdAt' | 'updateAt' | 'commandes'>;

type ProduitFormGroupContent = {
  id: FormControl<ProduitFormRawValue['id'] | NewProduit['id']>;
  nomProduit: FormControl<ProduitFormRawValue['nomProduit']>;
  desc: FormControl<ProduitFormRawValue['desc']>;
  tarifUnit: FormControl<ProduitFormRawValue['tarifUnit']>;
  date: FormControl<ProduitFormRawValue['date']>;
  duree: FormControl<ProduitFormRawValue['duree']>;
  lienImg: FormControl<ProduitFormRawValue['lienImg']>;
  quantiteTotale: FormControl<ProduitFormRawValue['quantiteTotale']>;
  quantiteDispo: FormControl<ProduitFormRawValue['quantiteDispo']>;
  createdAt: FormControl<ProduitFormRawValue['createdAt']>;
  updateAt: FormControl<ProduitFormRawValue['updateAt']>;
  nomProf: FormControl<ProduitFormRawValue['nomProf']>;
  promotion: FormControl<ProduitFormRawValue['promotion']>;
  souscategorie: FormControl<ProduitFormRawValue['souscategorie']>;
  commandes: FormControl<ProduitFormRawValue['commandes']>;
};

export type ProduitFormGroup = FormGroup<ProduitFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProduitFormService {
  createProduitFormGroup(produit: ProduitFormGroupInput = { id: null }): ProduitFormGroup {
    const produitRawValue = this.convertProduitToProduitRawValue({
      ...this.getFormDefaults(),
      ...produit,
    });
    return new FormGroup<ProduitFormGroupContent>({
      id: new FormControl(
        { value: produitRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomProduit: new FormControl(produitRawValue.nomProduit),
      desc: new FormControl(produitRawValue.desc),
      tarifUnit: new FormControl(produitRawValue.tarifUnit),
      date: new FormControl(produitRawValue.date),
      duree: new FormControl(produitRawValue.duree),
      lienImg: new FormControl(produitRawValue.lienImg),
      quantiteTotale: new FormControl(produitRawValue.quantiteTotale),
      quantiteDispo: new FormControl(produitRawValue.quantiteDispo),
      createdAt: new FormControl(produitRawValue.createdAt),
      updateAt: new FormControl(produitRawValue.updateAt),
      nomProf: new FormControl(produitRawValue.nomProf),
      promotion: new FormControl(produitRawValue.promotion),
      souscategorie: new FormControl(produitRawValue.souscategorie, {
        validators: [Validators.required],
      }),
      commandes: new FormControl(produitRawValue.commandes ?? []),
    });
  }

  getProduit(form: ProduitFormGroup): IProduit | NewProduit {
    return this.convertProduitRawValueToProduit(form.getRawValue() as ProduitFormRawValue | NewProduitFormRawValue);
  }

  resetForm(form: ProduitFormGroup, produit: ProduitFormGroupInput): void {
    const produitRawValue = this.convertProduitToProduitRawValue({ ...this.getFormDefaults(), ...produit });
    form.reset(
      {
        ...produitRawValue,
        id: { value: produitRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProduitFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      createdAt: currentTime,
      updateAt: currentTime,
      commandes: [],
    };
  }

  private convertProduitRawValueToProduit(rawProduit: ProduitFormRawValue | NewProduitFormRawValue): IProduit | NewProduit {
    return {
      ...rawProduit,
      date: dayjs(rawProduit.date, DATE_TIME_FORMAT),
      createdAt: dayjs(rawProduit.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawProduit.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertProduitToProduitRawValue(
    produit: IProduit | (Partial<NewProduit> & ProduitFormDefaults)
  ): ProduitFormRawValue | PartialWithRequiredKeyOf<NewProduitFormRawValue> {
    return {
      ...produit,
      date: produit.date ? produit.date.format(DATE_TIME_FORMAT) : undefined,
      createdAt: produit.createdAt ? produit.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: produit.updateAt ? produit.updateAt.format(DATE_TIME_FORMAT) : undefined,
      commandes: produit.commandes ?? [],
    };
  }
}
