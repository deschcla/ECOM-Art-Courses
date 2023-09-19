import dayjs from 'dayjs/esm';
import { IProduit } from 'app/entities/produit/produit.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface ILigneCommande {
  id: number;
  quantite?: number | null;
  montant?: number | null;
  validated?: number | null;
  nomParticipant?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  panier?: true | false;
  produit?: IProduit | null;
  commande?: ICommande | null;
}

export type NewLigneCommande = Omit<ILigneCommande, 'id'> & { id: null };
