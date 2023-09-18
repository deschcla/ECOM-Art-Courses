import dayjs from 'dayjs/esm';

import { IProduit, NewProduit } from './produit.model';

export const sampleWithRequiredData: IProduit = {
  id: 91013,
};

export const sampleWithPartialData: IProduit = {
  id: 66548,
  date: dayjs('2023-09-14T01:43'),
  lienImg: 'Automotive Shoes',
  quantiteTotale: 6905,
};

export const sampleWithFullData: IProduit = {
  id: 45999,
  nomProduit: 'violet Sleek input',
  desc: 'purple Union',
  tarifUnit: 90458,
  date: dayjs('2023-09-14T14:35'),
  duree: 'leverage',
  lienImg: 'sensor',
  quantiteTotale: 5,
  quantiteDispo: 42304,
  createdAt: dayjs('2023-09-14T15:00'),
  updateAt: dayjs('2023-09-14T22:09'),
  nomProf: 'Jeanne Darc',
  promotion: '10',
};

export const sampleWithNewData: NewProduit = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
