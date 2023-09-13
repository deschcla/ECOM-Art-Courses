import dayjs from 'dayjs/esm';

import { ICategorie, NewCategorie } from './categorie.model';

export const sampleWithRequiredData: ICategorie = {
  id: 30006,
};

export const sampleWithPartialData: ICategorie = {
  id: 20945,
};

export const sampleWithFullData: ICategorie = {
  id: 2824,
  typeCategorie: 'gifted',
  createdAt: dayjs('2023-09-11T12:55'),
  updateAt: dayjs('2023-09-11T23:42'),
};

export const sampleWithNewData: NewCategorie = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
