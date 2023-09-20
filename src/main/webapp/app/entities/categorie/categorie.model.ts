import dayjs from 'dayjs/esm';

export interface ICategorie {
  id: number;
  typeCategorie?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
}

export type NewCategorie = Omit<ICategorie, 'id'> & { id: null };
