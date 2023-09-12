import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICommande, NewCommande } from '../commande.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommande for edit and NewCommandeFormGroupInput for create.
 */
type CommandeFormGroupInput = ICommande | PartialWithRequiredKeyOf<NewCommande>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICommande | NewCommande> = Omit<T, 'createdAt' | 'updateAt'> & {
  createdAt?: string | null;
  updateAt?: string | null;
};

type CommandeFormRawValue = FormValueOf<ICommande>;

type NewCommandeFormRawValue = FormValueOf<NewCommande>;

type CommandeFormDefaults = Pick<NewCommande, 'id' | 'createdAt' | 'updateAt' | 'produits'>;

type CommandeFormGroupContent = {
  id: FormControl<CommandeFormRawValue['id'] | NewCommande['id']>;
  montant: FormControl<CommandeFormRawValue['montant']>;
  validated: FormControl<CommandeFormRawValue['validated']>;
  createdAt: FormControl<CommandeFormRawValue['createdAt']>;
  updateAt: FormControl<CommandeFormRawValue['updateAt']>;
  produits: FormControl<CommandeFormRawValue['produits']>;
  releveFacture: FormControl<CommandeFormRawValue['releveFacture']>;
  acheteur: FormControl<CommandeFormRawValue['acheteur']>;
};

export type CommandeFormGroup = FormGroup<CommandeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommandeFormService {
  createCommandeFormGroup(commande: CommandeFormGroupInput = { id: null }): CommandeFormGroup {
    const commandeRawValue = this.convertCommandeToCommandeRawValue({
      ...this.getFormDefaults(),
      ...commande,
    });
    return new FormGroup<CommandeFormGroupContent>({
      id: new FormControl(
        { value: commandeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      montant: new FormControl(commandeRawValue.montant),
      validated: new FormControl(commandeRawValue.validated),
      createdAt: new FormControl(commandeRawValue.createdAt),
      updateAt: new FormControl(commandeRawValue.updateAt),
      produits: new FormControl(commandeRawValue.produits ?? []),
      releveFacture: new FormControl(commandeRawValue.releveFacture, {
        validators: [Validators.required],
      }),
      acheteur: new FormControl(commandeRawValue.acheteur, {
        validators: [Validators.required],
      }),
    });
  }

  getCommande(form: CommandeFormGroup): ICommande | NewCommande {
    return this.convertCommandeRawValueToCommande(form.getRawValue() as CommandeFormRawValue | NewCommandeFormRawValue);
  }

  resetForm(form: CommandeFormGroup, commande: CommandeFormGroupInput): void {
    const commandeRawValue = this.convertCommandeToCommandeRawValue({ ...this.getFormDefaults(), ...commande });
    form.reset(
      {
        ...commandeRawValue,
        id: { value: commandeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CommandeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updateAt: currentTime,
      produits: [],
    };
  }

  private convertCommandeRawValueToCommande(rawCommande: CommandeFormRawValue | NewCommandeFormRawValue): ICommande | NewCommande {
    return {
      ...rawCommande,
      createdAt: dayjs(rawCommande.createdAt, DATE_TIME_FORMAT),
      updateAt: dayjs(rawCommande.updateAt, DATE_TIME_FORMAT),
    };
  }

  private convertCommandeToCommandeRawValue(
    commande: ICommande | (Partial<NewCommande> & CommandeFormDefaults)
  ): CommandeFormRawValue | PartialWithRequiredKeyOf<NewCommandeFormRawValue> {
    return {
      ...commande,
      createdAt: commande.createdAt ? commande.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updateAt: commande.updateAt ? commande.updateAt.format(DATE_TIME_FORMAT) : undefined,
      produits: commande.produits ?? [],
    };
  }
}
