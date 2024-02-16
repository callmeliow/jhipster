import dayjs from 'dayjs';

export interface ICustom {
  id?: number;
  ingredientName?: string;
  additionalCost?: number | null;
  imageUrl?: string;
  createdDate?: dayjs.Dayjs;
  updatedDate?: dayjs.Dayjs | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ICustom> = {};
