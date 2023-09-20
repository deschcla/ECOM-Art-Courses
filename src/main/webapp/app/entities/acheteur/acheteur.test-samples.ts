import dayjs from 'dayjs/esm';

import { IAcheteur, NewAcheteur } from './acheteur.model';

export const sampleWithRequiredData: IAcheteur = {
  id: 93347,
};

export const sampleWithPartialData: IAcheteur = {
  id: 80619,
  dateNaiss: dayjs('2023-09-14'),
  numTel: 'Steel Quality',
};

export const sampleWithFullData: IAcheteur = {
  id: 56030,
  adresse: 'AI program Engineer',
  dateNaiss: dayjs('2023-09-14'),
  numTel: 'front-end Highway Assistant',
  createdAt: dayjs('2023-09-14T09:44'),
  updateAt: dayjs('2023-09-14T20:04'),
};

export const sampleWithNewData: NewAcheteur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
