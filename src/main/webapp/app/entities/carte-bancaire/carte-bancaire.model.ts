import dayjs from 'dayjs/esm';
import { ICommande } from 'app/entities/commande/commande.model';

export interface ICarteBancaire {
  id: number;
  refCarte?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  commande?: Pick<ICommande, 'id'> | null;
}

export type NewCarteBancaire = Omit<ICarteBancaire, 'id'> & { id: null };
