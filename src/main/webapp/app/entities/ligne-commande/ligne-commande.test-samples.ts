import dayjs from 'dayjs/esm';

import { ILigneCommande, NewLigneCommande } from './ligne-commande.model';

export const sampleWithRequiredData: ILigneCommande = {
  id: 93307,
};

export const sampleWithPartialData: ILigneCommande = {
  id: 6875,
  quantite: 65420,
  createdAt: dayjs('2023-09-14T10:38'),
  updateAt: dayjs('2023-09-14T11:15'),
};

export const sampleWithFullData: ILigneCommande = {
  id: 8051,
  quantite: 38050,
  montant: 71483,
  validated: 28612,
  nomParticipant: 'Personal systematic',
  createdAt: dayjs('2023-09-14T12:21'),
  updateAt: dayjs('2023-09-14T05:22'),
  panier: true,
};

export const sampleWithNewData: NewLigneCommande = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
