import dayjs from 'dayjs/esm';

import { ILigneCommande, NewLigneCommande } from './ligne-commande.model';

export const sampleWithRequiredData: ILigneCommande = {
  id: 25027,
};

export const sampleWithPartialData: ILigneCommande = {
  id: 20613,
  quantite: 19125,
  montant: 7315,
  valided: 30050,
  updateAt: dayjs('2023-09-11T19:13'),
};

export const sampleWithFullData: ILigneCommande = {
  id: 11851,
  quantite: 3533,
  montant: 4797,
  valided: 5854,
  createdAt: dayjs('2023-09-11T14:49'),
  updateAt: dayjs('2023-09-11T08:26'),
};

export const sampleWithNewData: NewLigneCommande = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
