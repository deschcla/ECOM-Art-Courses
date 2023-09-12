import dayjs from 'dayjs/esm';

import { ISousCategorie, NewSousCategorie } from './sous-categorie.model';

export const sampleWithRequiredData: ISousCategorie = {
  id: 9369,
};

export const sampleWithPartialData: ISousCategorie = {
  id: 29499,
  createdAt: dayjs('2023-09-11T09:11'),
  updateAt: dayjs('2023-09-11T19:05'),
};

export const sampleWithFullData: ISousCategorie = {
  id: 13659,
  typeSousCategorie: 'Avon West Steel',
  createdAt: dayjs('2023-09-11T20:35'),
  updateAt: dayjs('2023-09-12T01:24'),
};

export const sampleWithNewData: NewSousCategorie = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
