import dayjs from 'dayjs/esm';

import { ICarteBancaire, NewCarteBancaire } from './carte-bancaire.model';

export const sampleWithRequiredData: ICarteBancaire = {
  id: 10344,
};

export const sampleWithPartialData: ICarteBancaire = {
  id: 3624,
  refCarte: 'bluetooth deposit',
};

export const sampleWithFullData: ICarteBancaire = {
  id: 733,
  refCarte: 'consequuntur systems Southeast',
  createdAt: dayjs('2023-09-12T03:06'),
  updateAt: dayjs('2023-09-11T17:05'),
};

export const sampleWithNewData: NewCarteBancaire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
