import dayjs from 'dayjs/esm';

import { IReleveFacture, NewReleveFacture } from './releve-facture.model';

export const sampleWithRequiredData: IReleveFacture = {
  id: 13990,
};

export const sampleWithPartialData: IReleveFacture = {
  id: 10697,
  createdAt: dayjs('2023-09-11T13:29'),
};

export const sampleWithFullData: IReleveFacture = {
  id: 12655,
  montant: 3411,
  createdAt: dayjs('2023-09-12T06:01'),
  updateAt: dayjs('2023-09-11T22:08'),
};

export const sampleWithNewData: NewReleveFacture = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
