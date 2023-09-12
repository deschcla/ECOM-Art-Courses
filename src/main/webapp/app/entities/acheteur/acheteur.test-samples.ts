import dayjs from 'dayjs/esm';

import { IAcheteur, NewAcheteur } from './acheteur.model';

export const sampleWithRequiredData: IAcheteur = {
  id: 7844,
};

export const sampleWithPartialData: IAcheteur = {
  id: 29824,
  adresse: 'Borders Northeast Wooden',
  createdAt: dayjs('2023-09-12T00:03'),
};

export const sampleWithFullData: IAcheteur = {
  id: 12802,
  adresse: 'South bypassing suspiciously',
  dateNaiss: dayjs('2023-09-11'),
  numTel: 'Cruiser modest',
  createdAt: dayjs('2023-09-12T07:24'),
  updateAt: dayjs('2023-09-11T22:06'),
};

export const sampleWithNewData: NewAcheteur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
