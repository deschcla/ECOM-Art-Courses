import dayjs from 'dayjs/esm';

import { ICarteBancaire, NewCarteBancaire } from './carte-bancaire.model';

export const sampleWithRequiredData: ICarteBancaire = {
  id: 72915,
};

export const sampleWithPartialData: ICarteBancaire = {
  id: 40633,
  refCarte: 'transmitting',
  createdAt: dayjs('2023-09-14T07:20'),
  updateAt: dayjs('2023-09-14T22:04'),
};

export const sampleWithFullData: ICarteBancaire = {
  id: 3909,
  refCarte: 'EXE',
  createdAt: dayjs('2023-09-14T13:46'),
  updateAt: dayjs('2023-09-14T08:19'),
};

export const sampleWithNewData: NewCarteBancaire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
