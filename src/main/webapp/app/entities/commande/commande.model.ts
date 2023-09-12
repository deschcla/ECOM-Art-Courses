import dayjs from 'dayjs/esm';
import { IProduit } from 'app/entities/produit/produit.model';
import { IReleveFacture } from 'app/entities/releve-facture/releve-facture.model';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';

export interface ICommande {
  id: number;
  montant?: number | null;
  valided?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  produits?: Pick<IProduit, 'id'>[] | null;
  releveFacture?: Pick<IReleveFacture, 'id'> | null;
  acheteur?: Pick<IAcheteur, 'id'> | null;
}

export type NewCommande = Omit<ICommande, 'id'> & { id: null };
