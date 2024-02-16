import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderItemCustom from './order-item-custom';
import OrderItemCustomDetail from './order-item-custom-detail';
import OrderItemCustomUpdate from './order-item-custom-update';
import OrderItemCustomDeleteDialog from './order-item-custom-delete-dialog';

const OrderItemCustomRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderItemCustom />} />
    <Route path="new" element={<OrderItemCustomUpdate />} />
    <Route path=":id">
      <Route index element={<OrderItemCustomDetail />} />
      <Route path="edit" element={<OrderItemCustomUpdate />} />
      <Route path="delete" element={<OrderItemCustomDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderItemCustomRoutes;
