import dayjs from 'dayjs/esm';
import { ISousCategorie } from 'app/entities/sous-categorie/sous-categorie.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface IProduit {
  id: number;
  nomProduit?: string | null;
  desc?: string | null;
  tarifUnit?: number | null;
  date?: dayjs.Dayjs | null;
  duree?: string | null;
  lienImg?: string | null;
  quantiteTotale?: number | null;
  quantiteDispo?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  nomProf?: string | null;
  promotion?: string | null;
  version?: number | null;
  souscategorie?: Pick<ISousCategorie, 'id'> | null;
  commandes?: Pick<ICommande, 'id'>[] | null;
}

export type NewProduit = Omit<IProduit, 'id'> & { id: null };
