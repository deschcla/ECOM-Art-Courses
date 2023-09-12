import dayjs from 'dayjs/esm';

import { ICommande, NewCommande } from './commande.model';

export const sampleWithRequiredData: ICommande = {
  id: 27769,
};

export const sampleWithPartialData: ICommande = {
  id: 2622,
  updateAt: dayjs('2023-09-12T06:22'),
};

export const sampleWithFullData: ICommande = {
  id: 9586,
  montant: 30777,
  validated: 11954,
  createdAt: dayjs('2023-09-12T00:46'),
  updateAt: dayjs('2023-09-11T21:28'),
};

export const sampleWithNewData: NewCommande = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
