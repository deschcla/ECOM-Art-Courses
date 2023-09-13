import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICategorie, NewCategorie } from '../categorie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategorie for edit and NewCategorieFormGroupInput for create.
 */
type CategorieFormGroupInput = ICategorie | PartialWithRequiredKeyOf<NewCategorie>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICategorie | NewCategorie> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

type CategorieFormRawValue = FormValueOf<ICategorie>;

type NewCategorieFormRawValue = FormValueOf<NewCategorie>;

type CategorieFormDefaults = Pick<NewCategorie, 'id' | 'createdAt' | 'updateAt'>;

type CategorieFormGroupContent = {
  id: FormControl<CategorieFormRawValue['id'] | NewCategorie['id']>;
  typeCategorie: FormControl<CategorieFormRawValue['typeCategorie']>;
  createdAt: FormControl<CategorieFormRawValue['createdAt']>;
  updateAt: FormControl<CategorieFormRawValue['updateAt']>;
};

export type CategorieFormGroup = FormGroup<CategorieFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategorieFormService {
  createCategorieFormGroup(categorie: CategorieFormGroupInput = { id: null }): CategorieFormGroup {
    const categorieRawValue = this.convertCategorieToCategorieRawValue({
      ...this.getFormDefaults(),
      ...categorie,
    });
    return new FormGroup<CategorieFormGroupContent>({
      id: new FormControl(
        { value: categorieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      typeCategorie: new FormControl(categorieRawValue.typeCategorie),
      createdAt: new FormControl(categorieRawValue.createdAt),
      updateAt: new FormControl(categorieRawValue.updateAt),
    });
  }

  getCategorie(form: CategorieFormGroup): ICategorie | NewCategorie {
    return this.convertCategorieRawValueToCategorie(form.getRawValue() as CategorieFormRawValue | NewCategorieFormRawValue);
  }

  resetForm(form: CategorieFormGroup, categorie: CategorieFormGroupInput): void {
    const categorieRawValue = this.convertCategorieToCategorieRawValue({ ...this.getFormDefaults(), ...categorie });
    form.reset(
      {
        ...categorieRawValue,
        id: { value: categorieRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategorieFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updateAt: currentTime,
    };
  }

  private convertCategorieRawValueToCategorie(rawCategorie: CategorieFormRawValue | NewCategorieFormRawValue): ICategorie | NewCategorie {
    return {
      ...rawCategorie,
      createdAt: dayjs(rawCategorie.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawCategorie.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertCategorieToCategorieRawValue(
    categorie: ICategorie | (Partial<NewCategorie> & CategorieFormDefaults)
  ): CategorieFormRawValue | PartialWithRequiredKeyOf<NewCategorieFormRawValue> {
    return {
      ...categorie,
      createdAt: categorie.createdAt ? categorie.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: categorie.updateAt ? categorie.updateAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
