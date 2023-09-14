import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IAcheteur {
  id: number;
  adresse?: string | null;
  dateNaiss?: dayjs.Dayjs | null;
  numTel?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewAcheteur = Omit<IAcheteur, 'id'> & { id: null };
