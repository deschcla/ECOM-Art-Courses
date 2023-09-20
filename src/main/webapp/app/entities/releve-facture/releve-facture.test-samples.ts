import dayjs from 'dayjs/esm';

import { IReleveFacture, NewReleveFacture } from './releve-facture.model';

export const sampleWithRequiredData: IReleveFacture = {
  id: 17175,
};

export const sampleWithPartialData: IReleveFacture = {
  id: 55764,
  montant: 8533,
  createdAt: dayjs('2023-09-14T07:34'),
  updateAt: dayjs('2023-09-14T15:19'),
};

export const sampleWithFullData: IReleveFacture = {
  id: 33631,
  montant: 83848,
  createdAt: dayjs('2023-09-14T14:05'),
  updateAt: dayjs('2023-09-14T22:20'),
};

export const sampleWithNewData: NewReleveFacture = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
