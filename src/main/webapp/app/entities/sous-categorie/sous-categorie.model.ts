import dayjs from 'dayjs/esm';
import { ICategorie } from 'app/entities/categorie/categorie.model';

export interface ISousCategorie {
  id: number;
  typeSousCategorie?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  categorie?: Pick<ICategorie, 'id'> | null;
}

export type NewSousCategorie = Omit<ISousCategorie, 'id'> & { id: null };
