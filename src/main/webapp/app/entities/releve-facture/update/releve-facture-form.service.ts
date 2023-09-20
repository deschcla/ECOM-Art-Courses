import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReleveFacture, NewReleveFacture } from '../releve-facture.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReleveFacture for edit and NewReleveFactureFormGroupInput for create.
 */
type ReleveFactureFormGroupInput = IReleveFacture | PartialWithRequiredKeyOf<NewReleveFacture>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReleveFacture | NewReleveFacture> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

type ReleveFactureFormRawValue = FormValueOf<IReleveFacture>;

type NewReleveFactureFormRawValue = FormValueOf<NewReleveFacture>;

type ReleveFactureFormDefaults = Pick<NewReleveFacture, 'id' | 'createdAt' | 'updateAt'>;

type ReleveFactureFormGroupContent = {
  id: FormControl<ReleveFactureFormRawValue['id'] | NewReleveFacture['id']>;
  montant: FormControl<ReleveFactureFormRawValue['montant']>;
  createdAt: FormControl<ReleveFactureFormRawValue['createdAt']>;
  updateAt: FormControl<ReleveFactureFormRawValue['updateAt']>;
  acheteur: FormControl<ReleveFactureFormRawValue['acheteur']>;
};

export type ReleveFactureFormGroup = FormGroup<ReleveFactureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReleveFactureFormService {
  createReleveFactureFormGroup(releveFacture: ReleveFactureFormGroupInput = { id: null }): ReleveFactureFormGroup {
    const releveFactureRawValue = this.convertReleveFactureToReleveFactureRawValue({
      ...this.getFormDefaults(),
      ...releveFacture,
    });
    return new FormGroup<ReleveFactureFormGroupContent>({
      id: new FormControl(
        { value: releveFactureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      montant: new FormControl(releveFactureRawValue.montant),
      createdAt: new FormControl(releveFactureRawValue.createdAt),
      updateAt: new FormControl(releveFactureRawValue.updateAt),
      acheteur: new FormControl(releveFactureRawValue.acheteur, {
        validators: [Validators.required],
      }),
    });
  }

  getReleveFacture(form: ReleveFactureFormGroup): IReleveFacture | NewReleveFacture {
    return this.convertReleveFactureRawValueToReleveFacture(form.getRawValue() as ReleveFactureFormRawValue | NewReleveFactureFormRawValue);
  }

  resetForm(form: ReleveFactureFormGroup, releveFacture: ReleveFactureFormGroupInput): void {
    const releveFactureRawValue = this.convertReleveFactureToReleveFactureRawValue({ ...this.getFormDefaults(), ...releveFacture });
    form.reset(
      {
        ...releveFactureRawValue,
        id: { value: releveFactureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReleveFactureFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updateAt: currentTime,
    };
  }

  private convertReleveFactureRawValueToReleveFacture(
    rawReleveFacture: ReleveFactureFormRawValue | NewReleveFactureFormRawValue
  ): IReleveFacture | NewReleveFacture {
    return {
      ...rawReleveFacture,
      createdAt: dayjs(rawReleveFacture.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawReleveFacture.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertReleveFactureToReleveFactureRawValue(
    releveFacture: IReleveFacture | (Partial<NewReleveFacture> & ReleveFactureFormDefaults)
  ): ReleveFactureFormRawValue | PartialWithRequiredKeyOf<NewReleveFactureFormRawValue> {
    return {
      ...releveFacture,
      createdAt: releveFacture.createdAt ? releveFacture.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: releveFacture.updateAt ? releveFacture.updateAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
