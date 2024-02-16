import { IOrder } from 'app/shared/model/order.model';

export interface IOrderFood {
  id?: number;
  foodName?: string;
  price?: number | null;
  orderItemPrice?: number | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IOrderFood> = {};
