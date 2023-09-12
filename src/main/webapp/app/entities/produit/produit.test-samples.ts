import dayjs from 'dayjs/esm';

import { IProduit, NewProduit } from './produit.model';

export const sampleWithRequiredData: IProduit = {
  id: 3694,
};

export const sampleWithPartialData: IProduit = {
  id: 24944,
  nomProduit: 'Bike Jaguar',
  desc: 'Lead Xenon',
  tarifUnit: 18163,
  date: dayjs('2023-09-11T14:05'),
  duree: 'Southwest Fantastic Northwest',
  lienImg: 'National near transparent',
  quantiteDispo: 20686,
  updateAt: dayjs('2023-09-11T12:44'),
};

export const sampleWithFullData: IProduit = {
  id: 102,
  nomProduit: 'bypass',
  desc: 'Intersex',
  tarifUnit: 5563,
  date: dayjs('2023-09-12T03:50'),
  duree: 'Southwest',
  lienImg: 'East',
  quantiteTotale: 16419,
  quantiteDispo: 26822,
  createdAt: dayjs('2023-09-12T01:49'),
  updateAt: dayjs('2023-09-11T22:22'),
};

export const sampleWithNewData: NewProduit = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
