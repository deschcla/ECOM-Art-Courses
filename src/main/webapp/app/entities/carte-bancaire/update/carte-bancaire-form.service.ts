import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICarteBancaire, NewCarteBancaire } from '../carte-bancaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICarteBancaire for edit and NewCarteBancaireFormGroupInput for create.
 */
type CarteBancaireFormGroupInput = ICarteBancaire | PartialWithRequiredKeyOf<NewCarteBancaire>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICarteBancaire | NewCarteBancaire> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

type CarteBancaireFormRawValue = FormValueOf<ICarteBancaire>;

type NewCarteBancaireFormRawValue = FormValueOf<NewCarteBancaire>;

type CarteBancaireFormDefaults = Pick<NewCarteBancaire, 'id' | 'createdAt' | 'updateAt'>;

type CarteBancaireFormGroupContent = {
  id: FormControl<CarteBancaireFormRawValue['id'] | NewCarteBancaire['id']>;
  refCarte: FormControl<CarteBancaireFormRawValue['refCarte']>;
  createdAt: FormControl<CarteBancaireFormRawValue['createdAt']>;
  updateAt: FormControl<CarteBancaireFormRawValue['updateAt']>;
  commande: FormControl<CarteBancaireFormRawValue['commande']>;
};

export type CarteBancaireFormGroup = FormGroup<CarteBancaireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CarteBancaireFormService {
  createCarteBancaireFormGroup(carteBancaire: CarteBancaireFormGroupInput = { id: null }): CarteBancaireFormGroup {
    const carteBancaireRawValue = this.convertCarteBancaireToCarteBancaireRawValue({
      ...this.getFormDefaults(),
      ...carteBancaire,
    });
    return new FormGroup<CarteBancaireFormGroupContent>({
      id: new FormControl(
        { value: carteBancaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      refCarte: new FormControl(carteBancaireRawValue.refCarte),
      createdAt: new FormControl(carteBancaireRawValue.createdAt),
      updateAt: new FormControl(carteBancaireRawValue.updateAt),
      commande: new FormControl(carteBancaireRawValue.commande, {
        validators: [Validators.required],
      }),
    });
  }

  getCarteBancaire(form: CarteBancaireFormGroup): ICarteBancaire | NewCarteBancaire {
    return this.convertCarteBancaireRawValueToCarteBancaire(form.getRawValue() as CarteBancaireFormRawValue | NewCarteBancaireFormRawValue);
  }

  resetForm(form: CarteBancaireFormGroup, carteBancaire: CarteBancaireFormGroupInput): void {
    const carteBancaireRawValue = this.convertCarteBancaireToCarteBancaireRawValue({ ...this.getFormDefaults(), ...carteBancaire });
    form.reset(
      {
        ...carteBancaireRawValue,
        id: { value: carteBancaireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CarteBancaireFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updateAt: currentTime,
    };
  }

  private convertCarteBancaireRawValueToCarteBancaire(
    rawCarteBancaire: CarteBancaireFormRawValue | NewCarteBancaireFormRawValue
  ): ICarteBancaire | NewCarteBancaire {
    return {
      ...rawCarteBancaire,
      createdAt: dayjs(rawCarteBancaire.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawCarteBancaire.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertCarteBancaireToCarteBancaireRawValue(
    carteBancaire: ICarteBancaire | (Partial<NewCarteBancaire> & CarteBancaireFormDefaults)
  ): CarteBancaireFormRawValue | PartialWithRequiredKeyOf<NewCarteBancaireFormRawValue> {
    return {
      ...carteBancaire,
      createdAt: carteBancaire.createdAt ? carteBancaire.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: carteBancaire.updateAt ? carteBancaire.updateAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
