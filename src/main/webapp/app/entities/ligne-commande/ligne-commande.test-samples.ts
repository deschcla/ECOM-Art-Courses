import dayjs from 'dayjs/esm';

import { ILigneCommande, NewLigneCommande } from './ligne-commande.model';

export const sampleWithRequiredData: ILigneCommande = {
  id: 4423,
};

export const sampleWithPartialData: ILigneCommande = {
  id: 7315,
  quantite: 30050,
  montant: 16926,
  nomParticipant: 'Delaware Clothing',
};

export const sampleWithFullData: ILigneCommande = {
  id: 31655,
  quantite: 26779,
  montant: 31338,
  validated: 25991,
  nomParticipant: 'Loan toolset',
  createdAt: dayjs('2023-09-12T00:31'),
  updateAt: dayjs('2023-09-11T09:03'),
};

export const sampleWithNewData: NewLigneCommande = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
