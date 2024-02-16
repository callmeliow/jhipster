import { IOrderItem } from 'app/shared/model/order-item.model';
import { ICustom } from 'app/shared/model/custom.model';

export interface IOrderItemCustom {
  id?: number;
  orderItem?: IOrderItem | null;
  custom?: ICustom | null;
}

export const defaultValue: Readonly<IOrderItemCustom> = {};
