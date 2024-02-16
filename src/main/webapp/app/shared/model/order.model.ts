import dayjs from 'dayjs';

export interface IOrder {
  id?: number;
  orderDate?: dayjs.Dayjs;
  totalAmount?: number | null;
}

export const defaultValue: Readonly<IOrder> = {};
