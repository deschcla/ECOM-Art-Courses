import dayjs from 'dayjs/esm';

import { ICommande, NewCommande } from './commande.model';

export const sampleWithRequiredData: ICommande = {
  id: 5189,
};

export const sampleWithPartialData: ICommande = {
  id: 99518,
  montant: 4218,
  createdAt: dayjs('2023-09-14T17:48'),
};

export const sampleWithFullData: ICommande = {
  id: 93952,
  montant: 76871,
  validated: 40062,
  createdAt: dayjs('2023-09-14T15:56'),
  updateAt: dayjs('2023-09-14T03:39'),
};

export const sampleWithNewData: NewCommande = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
