import dayjs from 'dayjs/esm';

import { ISousCategorie, NewSousCategorie } from './sous-categorie.model';

export const sampleWithRequiredData: ISousCategorie = {
  id: 3007,
};

export const sampleWithPartialData: ISousCategorie = {
  id: 17236,
  typeSousCategorie: 'bypass Granite',
  updateAt: dayjs('2023-09-14T08:00'),
};

export const sampleWithFullData: ISousCategorie = {
  id: 71077,
  typeSousCategorie: 'Web PCI Internal',
  createdAt: dayjs('2023-09-14T05:34'),
  updateAt: dayjs('2023-09-14T17:26'),
};

export const sampleWithNewData: NewSousCategorie = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
