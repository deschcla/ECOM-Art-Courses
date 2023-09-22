import dayjs from 'dayjs/esm';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';
import { IUser } from '../user/user.model';

export interface IReleveFacture {
  id: number;
  montant?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  acheteur?: IAcheteur | null;
  user?: IUser | null;
}

export type NewReleveFacture = Omit<IReleveFacture, 'id'> & { id: null };
