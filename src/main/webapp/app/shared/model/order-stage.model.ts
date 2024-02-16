import dayjs from 'dayjs';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrderStage {
  id?: number;
  orderNo?: string;
  orderDate?: dayjs.Dayjs;
  status?: keyof typeof OrderStatus;
  createdDate?: dayjs.Dayjs;
  updatedDate?: dayjs.Dayjs | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IOrderStage> = {};
