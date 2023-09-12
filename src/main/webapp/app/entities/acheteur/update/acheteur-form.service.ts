import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAcheteur, NewAcheteur } from '../acheteur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAcheteur for edit and NewAcheteurFormGroupInput for create.
 */
type AcheteurFormGroupInput = IAcheteur | PartialWithRequiredKeyOf<NewAcheteur>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAcheteur | NewAcheteur> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

type AcheteurFormRawValue = FormValueOf<IAcheteur>;

type NewAcheteurFormRawValue = FormValueOf<NewAcheteur>;

type AcheteurFormDefaults = Pick<NewAcheteur, 'id' | 'createdAt' | 'updateAt'>;

type AcheteurFormGroupContent = {
  id: FormControl<AcheteurFormRawValue['id'] | NewAcheteur['id']>;
  adresse: FormControl<AcheteurFormRawValue['adresse']>;
  dateNaiss: FormControl<AcheteurFormRawValue['dateNaiss']>;
  numTel: FormControl<AcheteurFormRawValue['numTel']>;
  createdAt: FormControl<AcheteurFormRawValue['createdAt']>;
  updateAt: FormControl<AcheteurFormRawValue['updateAt']>;
  internalUser: FormControl<AcheteurFormRawValue['internalUser']>;
};

export type AcheteurFormGroup = FormGroup<AcheteurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AcheteurFormService {
  createAcheteurFormGroup(acheteur: AcheteurFormGroupInput = { id: null }): AcheteurFormGroup {
    const acheteurRawValue = this.convertAcheteurToAcheteurRawValue({
      ...this.getFormDefaults(),
      ...acheteur,
    });
    return new FormGroup<AcheteurFormGroupContent>({
      id: new FormControl(
        { value: acheteurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      adresse: new FormControl(acheteurRawValue.adresse),
      dateNaiss: new FormControl(acheteurRawValue.dateNaiss),
      numTel: new FormControl(acheteurRawValue.numTel),
      createdAt: new FormControl(acheteurRawValue.createdAt),
      updateAt: new FormControl(acheteurRawValue.updateAt),
      internalUser: new FormControl(acheteurRawValue.internalUser, {
        validators: [Validators.required],
      }),
    });
  }

  getAcheteur(form: AcheteurFormGroup): IAcheteur | NewAcheteur {
    return this.convertAcheteurRawValueToAcheteur(form.getRawValue() as AcheteurFormRawValue | NewAcheteurFormRawValue);
  }

  resetForm(form: AcheteurFormGroup, acheteur: AcheteurFormGroupInput): void {
    const acheteurRawValue = this.convertAcheteurToAcheteurRawValue({ ...this.getFormDefaults(), ...acheteur });
    form.reset(
      {
        ...acheteurRawValue,
        id: { value: acheteurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AcheteurFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updateAt: currentTime,
    };
  }

  private convertAcheteurRawValueToAcheteur(rawAcheteur: AcheteurFormRawValue | NewAcheteurFormRawValue): IAcheteur | NewAcheteur {
    return {
      ...rawAcheteur,
      createdAt: dayjs(rawAcheteur.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawAcheteur.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertAcheteurToAcheteurRawValue(
    acheteur: IAcheteur | (Partial<NewAcheteur> & AcheteurFormDefaults)
  ): AcheteurFormRawValue | PartialWithRequiredKeyOf<NewAcheteurFormRawValue> {
    return {
      ...acheteur,
      createdAt: acheteur.createdAt ? acheteur.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: acheteur.updateAt ? acheteur.updateAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
