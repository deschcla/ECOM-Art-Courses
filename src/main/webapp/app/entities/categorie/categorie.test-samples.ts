import dayjs from 'dayjs/esm';

import { ICategorie, NewCategorie } from './categorie.model';

export const sampleWithRequiredData: ICategorie = {
  id: 37918,
};

export const sampleWithPartialData: ICategorie = {
  id: 82129,
  createdAt: dayjs('2023-09-14T05:55'),
};

export const sampleWithFullData: ICategorie = {
  id: 4633,
  typeCategorie: 'calculating Manager',
  createdAt: dayjs('2023-09-14T02:01'),
  updateAt: dayjs('2023-09-14T00:11'),
};

export const sampleWithNewData: NewCategorie = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
