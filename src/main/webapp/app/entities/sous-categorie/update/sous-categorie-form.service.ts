import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISousCategorie, NewSousCategorie } from '../sous-categorie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISousCategorie for edit and NewSousCategorieFormGroupInput for create.
 */
type SousCategorieFormGroupInput = ISousCategorie | PartialWithRequiredKeyOf<NewSousCategorie>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISousCategorie | NewSousCategorie> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

type SousCategorieFormRawValue = FormValueOf<ISousCategorie>;

type NewSousCategorieFormRawValue = FormValueOf<NewSousCategorie>;

type SousCategorieFormDefaults = Pick<NewSousCategorie, 'id' | 'createdAt' | 'updateAt'>;

type SousCategorieFormGroupContent = {
  id: FormControl<SousCategorieFormRawValue['id'] | NewSousCategorie['id']>;
  typeSousCategorie: FormControl<SousCategorieFormRawValue['typeSousCategorie']>;
  createdAt: FormControl<SousCategorieFormRawValue['createdAt']>;
  updateAt: FormControl<SousCategorieFormRawValue['updateAt']>;
  categorie: FormControl<SousCategorieFormRawValue['categorie']>;
};

export type SousCategorieFormGroup = FormGroup<SousCategorieFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SousCategorieFormService {
  createSousCategorieFormGroup(sousCategorie: SousCategorieFormGroupInput = { id: null }): SousCategorieFormGroup {
    const sousCategorieRawValue = this.convertSousCategorieToSousCategorieRawValue({
      ...this.getFormDefaults(),
      ...sousCategorie,
    });
    return new FormGroup<SousCategorieFormGroupContent>({
      id: new FormControl(
        { value: sousCategorieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      typeSousCategorie: new FormControl(sousCategorieRawValue.typeSousCategorie),
      createdAt: new FormControl(sousCategorieRawValue.createdAt),
      updateAt: new FormControl(sousCategorieRawValue.updateAt),
      categorie: new FormControl(sousCategorieRawValue.categorie, {
        validators: [Validators.required],
      }),
    });
  }

  getSousCategorie(form: SousCategorieFormGroup): ISousCategorie | NewSousCategorie {
    return this.convertSousCategorieRawValueToSousCategorie(form.getRawValue() as SousCategorieFormRawValue | NewSousCategorieFormRawValue);
  }

  resetForm(form: SousCategorieFormGroup, sousCategorie: SousCategorieFormGroupInput): void {
    const sousCategorieRawValue = this.convertSousCategorieToSousCategorieRawValue({ ...this.getFormDefaults(), ...sousCategorie });
    form.reset(
      {
        ...sousCategorieRawValue,
        id: { value: sousCategorieRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SousCategorieFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updateAt: currentTime,
    };
  }

  private convertSousCategorieRawValueToSousCategorie(
    rawSousCategorie: SousCategorieFormRawValue | NewSousCategorieFormRawValue
  ): ISousCategorie | NewSousCategorie {
    return {
      ...rawSousCategorie,
      createdAt: dayjs(rawSousCategorie.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawSousCategorie.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertSousCategorieToSousCategorieRawValue(
    sousCategorie: ISousCategorie | (Partial<NewSousCategorie> & SousCategorieFormDefaults)
  ): SousCategorieFormRawValue | PartialWithRequiredKeyOf<NewSousCategorieFormRawValue> {
    return {
      ...sousCategorie,
      createdAt: sousCategorie.createdAt ? sousCategorie.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: sousCategorie.updateAt ? sousCategorie.updateAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
