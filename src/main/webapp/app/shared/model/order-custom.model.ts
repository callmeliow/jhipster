import { IOrderFood } from 'app/shared/model/order-food.model';

export interface IOrderCustom {
  id?: number;
  customizationName?: string | null;
  price?: number | null;
  orderFood?: IOrderFood | null;
}

export const defaultValue: Readonly<IOrderCustom> = {};
