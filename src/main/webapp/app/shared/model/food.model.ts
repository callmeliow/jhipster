import dayjs from 'dayjs';

export interface IFood {
  id?: number;
  name?: string;
  price?: number;
  description?: string | null;
  available?: boolean;
  imageUrl?: string;
  createdDate?: dayjs.Dayjs;
  updatedDate?: dayjs.Dayjs | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IFood> = {
  available: false,
};
