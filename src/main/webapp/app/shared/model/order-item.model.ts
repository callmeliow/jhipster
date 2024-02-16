import { IFood } from 'app/shared/model/food.model';
import { IOrderStage } from 'app/shared/model/order-stage.model';

export interface IOrderItem {
  id?: number;
  food?: IFood | null;
  orderStage?: IOrderStage | null;
}

export const defaultValue: Readonly<IOrderItem> = {};
